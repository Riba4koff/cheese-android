package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.domain.animations.Animations
import ru.antares.cheese_android.domain.animations.enterHorizontallySlide
import ru.antares.cheese_android.domain.animations.exitHorizontallySlide
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailNavigationEvent
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail.ProductDetailViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsNavigationEvent
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.catalogNavigationGraph(
    catalogNavController: NavController,
    nextRoute: String?,
) {

    navigation(
        startDestination = Screen.CatalogNavigationGraph.Catalog.url,
        route = Screen.CatalogNavigationGraph.route
    ) {

        // Catalog
        composable(
            enterTransition = Animations.AnimateToRight.enter,
            exitTransition = Animations.AnimateToRight.exit,
            route = Screen.CatalogNavigationGraph.Catalog.route
        ) { navBackStackEntry ->
            val viewModel: CatalogViewModel =
                navBackStackEntry.sharedViewModel(navController = catalogNavController)
            val state by viewModel.state.collectAsStateWithLifecycle()

            CatalogScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onError = viewModel::onError,
                navController = catalogNavController,
                navigationEvents = viewModel.navigationEvents
            )
        }

        // Children categories
        composable(
            enterTransition = enterHorizontallySlide(Screen.CatalogNavigationGraph.Products.url),
            exitTransition = exitHorizontallySlide(nextRoute == Screen.CatalogNavigationGraph.Products.url),
            route = Screen.CatalogNavigationGraph.CatalogParentCategory.url,
            arguments = listOf(
                navArgument("parentID") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val parentID = navBackStackEntry.arguments?.getString("parentID") ?: ""
            val name = navBackStackEntry.arguments?.getString("name") ?: ""

            val viewModel: CatalogParentCategoryViewModel =
                koinViewModel(parameters = { parametersOf(parentID) })
            val state by viewModel.state.collectAsStateWithLifecycle()

            CatalogParentCategoryScreen(
                navController = catalogNavController,
                name = name,
                state = state,
                onError = viewModel::onError,
                onEvent = viewModel::onEvent,
                navigationEvents = viewModel.navigationEvent
            )
        }

        // Products
        composable(
            enterTransition = enterHorizontallySlide(Screen.CatalogNavigationGraph.ProductDetail.url),
            exitTransition = exitHorizontallySlide(nextRoute == Screen.CatalogNavigationGraph.ProductDetail.url),
            route = Screen.CatalogNavigationGraph.Products.url,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id") ?: ""
            val name = navBackStackEntry.arguments?.getString("name") ?: ""

            val viewModel: ProductsViewModel =
                koinViewModel(parameters = { parametersOf(id) })
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { event ->
                when (event) {
                    is ProductsNavigationEvent.NavigateToProductDetailInfo -> {
                        catalogNavController.navigate(Screen.CatalogNavigationGraph.ProductDetail.route + "/${event.product.id}")
                    }

                    ProductsNavigationEvent.GoBack -> {
                        catalogNavController.navigateUp()
                    }
                }
            }

            ProductsScreen(
                name = name,
                state = state,
                onError = viewModel::onError,
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
            route = Screen.CatalogNavigationGraph.ProductDetail.url,
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
                        catalogNavController.navigateUp()
                    }

                    is ProductDetailNavigationEvent.NavigateToFeedBack -> {
                        /* TODO: navigate to next product screen */
                    }

                    is ProductDetailNavigationEvent.NavigateToProduct -> {
                        catalogNavController.navigate(
                            route = Screen.CatalogNavigationGraph.ProductDetail.route + "/${navigationEvent.productID}"
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
                        ProductDetailNavigationEvent.NavigateToProduct(product.value.id)
                    )
                }
            )
        }
    }
}
