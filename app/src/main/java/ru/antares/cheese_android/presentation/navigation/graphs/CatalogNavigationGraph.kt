package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
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
import ru.antares.cheese_android.domain.Animations
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category.CatalogParentCategoryViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.catalogNavigationGraph(
    catalogNavController: NavController,
    nextRoute: String?
) {
    navigation(
        startDestination = Screen.CatalogNavigationGraph.Catalog.url,
        route = Screen.CatalogNavigationGraph.route
    ) {
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
        composable(
            enterTransition = {
                val previousDestination = initialState.destination.route
                if (previousDestination == Screen.CatalogNavigationGraph.Products.url) {
                    //animation from left to right
                    slideIntoContainer(
                        animationSpec = tween(Animations.ANIMATE_TIME, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    )
                } else {
                    //Appearance animation from right to left
                    slideIntoContainer(
                        animationSpec = tween(Animations.ANIMATE_TIME, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    )
                }
            },
            exitTransition = {
                if (nextRoute == Screen.CatalogNavigationGraph.Products.url) {
                    //animation from right to left
                    slideOutOfContainer(
                        animationSpec = tween(Animations.ANIMATE_TIME, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                } else {
                    //animation from left to right
                    slideOutOfContainer(
                        animationSpec = tween(Animations.ANIMATE_TIME, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                }
            },
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
            enterTransition = Animations.AnimateToLeft.enter,
            exitTransition = Animations.AnimateToLeft.exit,
            route = Screen.CatalogNavigationGraph.Products.url,
            arguments = listOf(navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id") ?: ""
            val name = navBackStackEntry.arguments?.getString("name") ?: ""

            val viewModel: ProductsViewModel =
                koinViewModel(parameters = { parametersOf(id) })
            val state by viewModel.state.collectAsStateWithLifecycle()

            ProductsScreen(
                name = name,
                navController = catalogNavController,
                state = state,
                onError = viewModel::onError,
                onEvent = viewModel::onEvent
            )
        }
    }
}
