package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_detail_category.CatalogParentCategoryScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_detail_category.CatalogParentCategoryViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.catalogNavigationGraph(catalogNavController: NavController) {
    navigation(
        startDestination = Screen.CatalogNavigationGraph.Catalog.url,
        route = Screen.CatalogNavigationGraph.route
    ) {
        composable(route = Screen.CatalogNavigationGraph.Catalog.route) { navBackStackEntry ->
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
            route = Screen.CatalogNavigationGraph.CatalogParentCategory.url,
            arguments = listOf(
                navArgument("parentID") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val parentID = navBackStackEntry.arguments?.getString("parentID") ?: ""
            val name = navBackStackEntry.arguments?.getString("name") ?: ""

            val viewModel: CatalogParentCategoryViewModel = navBackStackEntry.sharedViewModel(
                navController = catalogNavController,
                parameters = {
                    parametersOf(parentID)
                })
            val state by viewModel.state.collectAsStateWithLifecycle()

            CatalogParentCategoryScreen(
                navController = catalogNavController,
                name = name,
                catalogParentCategoryViewState = state,
                onError = viewModel::onError,
                onEvent = viewModel::onEvent,
                navigationEvents = viewModel.navigationEvent
            )
        }

        composable(
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