package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailNavigationEvent
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailViewModel
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityNavigationEvent
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityScreen
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityViewModel
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityNavigationEvent
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityScreen
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityViewModel
import ru.antares.cheese_android.presentation.view.main.community_graph.post.PostNavigationEvent
import ru.antares.cheese_android.presentation.view.main.community_graph.post.PostScreen
import ru.antares.cheese_android.presentation.view.main.community_graph.post.PostViewModel

fun NavGraphBuilder.communityNavigationGraph(communityNavController: NavController) {
    navigation(
        route = Screen.CommunityNavigationGraph.route,
        startDestination = Screen.CommunityNavigationGraph.Community.route
    ) {
        composable(
            route = Screen.CommunityNavigationGraph.Community.route,
            enterTransition = {
                fadeIn(tween(128))
            },
            exitTransition = {
                fadeOut(tween(128))
            }
        ) { _ ->
            val viewModel: CommunityViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    is CommunityNavigationEvent.NavigateToActivity -> {
                        communityNavController.navigate(
                            route = Screen.CommunityNavigationGraph.CommunityActivityScreen.route + "/${navigationEvent.postID}"
                        )
                    }

                    is CommunityNavigationEvent.NavigateToPost -> {
                        communityNavController.navigate(
                            route = Screen.CommunityNavigationGraph.CommunityPostScreen.route + "/${navigationEvent.postID}"
                        )
                    }
                }
            }

            CommunityScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }
        composable(
            route = Screen.CommunityNavigationGraph.CommunityPostScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(tween(128))
            },
            exitTransition = {
                fadeOut(tween(128))
            }
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id")
            val viewModel: PostViewModel = koinViewModel(
                parameters = {
                    parametersOf(id)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    PostNavigationEvent.NavigateBack -> {
                        communityNavController.navigateUp()
                    }

                    is PostNavigationEvent.NavigateToProduct -> {
                        communityNavController.navigate(
                            route = Screen.CommunityNavigationGraph.ProductDetailScreen.route + "/${navigationEvent.productID}"
                        )
                    }
                }
            }


            PostScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }
        composable(
            route = Screen.CommunityNavigationGraph.CommunityActivityScreen.url,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(tween(128))
            },
            exitTransition = {
                fadeOut(tween(128))
            }
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id")
            val viewModel: ActivityViewModel = koinViewModel(
                parameters = {
                    parametersOf(id)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    ActivityNavigationEvent.NavigateBack -> {
                        communityNavController.navigateUp()
                    }

                    is ActivityNavigationEvent.NavigateToProduct -> {
                        communityNavController.navigate(
                            route = Screen.CommunityNavigationGraph.ProductDetailScreen.route + "/${navigationEvent.productID}"
                        )
                    }
                }
            }

            ActivityScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }

        // Product detail info
        composable(
            enterTransition = {
                fadeIn(tween(128))
            },
            exitTransition = {
                fadeOut(tween(128))
            },
            route = Screen.CommunityNavigationGraph.ProductDetailScreen.url,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val productID = navBackStackEntry.arguments?.getString("id") ?: ""

            val viewModel: ProductDetailViewModel = koinViewModel(
                parameters = {
                    parametersOf(productID)
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    ProductDetailNavigationEvent.GoBack -> {
                        communityNavController.navigateUp()
                    }

                    is ProductDetailNavigationEvent.NavigateToFeedBack -> {
                        /* TODO: navigate to next product screen */
                    }

                    is ProductDetailNavigationEvent.NavigateToProduct -> {
                        communityNavController.navigate(
                            route = Screen.CommunityNavigationGraph.ProductDetailScreen.route + "/${navigationEvent.productID}"
                        )
                    }
                }
            }

            ProductDetailScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent,
                onError = viewModel::onError,
                navigateToProduct = { product ->
                    viewModel.onNavigationEvent(
                        ProductDetailNavigationEvent.NavigateToProduct(productID = product.value.id)
                    )
                }
            )
        }
    }
}