package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.catalogNavigationGraph(catalogNavController: NavController) {
    navigation(startDestination = Screen.CatalogNavigationGraph.Catalog.route, route = Screen.CatalogNavigationGraph.route) {
        composable(route = Screen.CatalogNavigationGraph.Catalog.route) { navBackStackEntry ->
            val viewModel: CatalogViewModel = navBackStackEntry.sharedViewModel(navController = catalogNavController)
            val state by viewModel.state.collectAsStateWithLifecycle()

            CatalogScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onError = viewModel::onError
            )
        }
    }
}