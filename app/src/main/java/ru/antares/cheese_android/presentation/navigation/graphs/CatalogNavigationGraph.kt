package ru.antares.cheese_android.presentation.navigation.graphs

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
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
    currentRoute: String?
) {
    Log.d("current_route", currentRoute.toString())

    navigation(
        startDestination = Screen.CatalogNavigationGraph.Catalog.url,
        route = Screen.CatalogNavigationGraph.route
    ) {
        composable(
            enterTransition = {
                Log.d("init", initialState.destination.route.toString())
                slideInHorizontally(tween(200)) { -it }
            },
            exitTransition = {
                slideOutHorizontally(tween(200)) { -it }
            },
            route = Screen.CatalogNavigationGraph.Catalog.route
        ) { navBackStackEntry ->
            val viewModel: CatalogViewModel = navBackStackEntry.sharedViewModel(
                navController = catalogNavController
            )
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
                slideInHorizontally(tween(200)) { it }
            },
            exitTransition = {
                slideOutHorizontally(tween(200)) { it }
            },
            route = Screen.CatalogNavigationGraph.CatalogParentCategory.url,
            arguments = listOf(
                navArgument("parentID") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val parentID = navBackStackEntry.arguments?.getString("parentID") ?: ""
            val name = navBackStackEntry.arguments?.getString("name") ?: ""

            val viewModel: CatalogParentCategoryViewModel = koinViewModel(
                parameters = {
                    parametersOf(parentID)
                }
            )
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

        composable(
            enterTransition = {
                slideInHorizontally(tween(200)) { it }
            },
            exitTransition = {
                slideOutHorizontally(tween(200)) { it }
            },
            route = Screen.CatalogNavigationGraph.Products.url,
            arguments = listOf(navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getString("id") ?: ""
            val name = navBackStackEntry.arguments?.getString("name") ?: ""

            val viewModel: ProductsViewModel = koinViewModel(
                parameters = {
                    parametersOf(id)
                }
            )
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