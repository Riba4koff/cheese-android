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
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityNavigationEvent
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityScreen
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityViewModel
import ru.antares.cheese_android.presentation.view.main.community_graph.post.PostNavigationEvent
import ru.antares.cheese_android.presentation.view.main.community_graph.post.PostScreen
import ru.antares.cheese_android.presentation.view.main.community_graph.post.PostViewModel
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreen
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreenNavigationEvent
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreenViewModel

fun NavGraphBuilder.homeNavigationGraph(
    navController: NavController,
) {
    navigation(
        route = Screen.HomeNavigationGraph.route,
        startDestination = Screen.HomeNavigationGraph.Home.route
    ) {
        composable(
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
            route = Screen.HomeNavigationGraph.Home.route
        ) { navBackStackEntry ->
            val viewModel: HomeScreenViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    is HomeScreenNavigationEvent.NavigateToActivity -> {
                        navController.navigate(Screen.HomeNavigationGraph.Activity.route + "/" + navigationEvent.activityId)
                    }
                    is HomeScreenNavigationEvent.NavigateToPost -> {
                        navController.navigate(Screen.HomeNavigationGraph.Post.route + "/" + navigationEvent.postId)
                    }
                    is HomeScreenNavigationEvent.NavigateToRecommendation -> {
                        navController.navigate(Screen.HomeNavigationGraph.ProductDetailScreen.route + "/" + navigationEvent.recommendationId)
                    }
                }
            }

            HomeScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }

        composable(
            route = Screen.HomeNavigationGraph.Activity.url,
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
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
                        navController.popBackStack()
                    }

                    is ActivityNavigationEvent.NavigateToProduct -> {
                        navController.navigate(
                            route = Screen.HomeNavigationGraph.ProductDetailScreen.route + "/${navigationEvent.productID}"
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

        composable(
            route = Screen.HomeNavigationGraph.Post.url,
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
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
                        navController.popBackStack()
                    }

                    is PostNavigationEvent.NavigateToProduct -> {
                        navController.navigate(
                            route = Screen.HomeNavigationGraph.ProductDetailScreen.route + "/${navigationEvent.productID}"
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
            route = Screen.HomeNavigationGraph.ProductDetailScreen.url,
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
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
                        navController.popBackStack()
                    }

                    is ProductDetailNavigationEvent.NavigateToFeedBack -> {
                        /* TODO: navigate to next product screen */
                    }

                    is ProductDetailNavigationEvent.NavigateToProduct -> {
                        navController.navigate(
                            route = Screen.HomeNavigationGraph.ProductDetailScreen.route + "/${navigationEvent.productID}"
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