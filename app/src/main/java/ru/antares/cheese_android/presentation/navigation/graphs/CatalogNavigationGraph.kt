package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.core.parameter.parametersOf
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogScreen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.catalogNavigationGraph(catalogNavController: NavController) {
    navigation(
        startDestination = Screen.CatalogNavigationGraph.Catalog.url,
        route = Screen.CatalogNavigationGraph.route
    ) {
        composable(
            route = Screen.CatalogNavigationGraph.Catalog.url,
            arguments = listOf(
                navArgument("parentID") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val parentID = navBackStackEntry.arguments?.getString("parentID") ?: ""

            val viewModel: CatalogViewModel = navBackStackEntry.sharedViewModel(
                navController = catalogNavController,
                parameters = { parametersOf(parentID) }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            CatalogScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onError = viewModel::onError
            )
        }
    }
}