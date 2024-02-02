package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.catalogNavigationGraph(catalogNavController: NavController) {
    navigation(startDestination = Screen.CatalogNavigationGraph.Catalog.route, route = Screen.CatalogNavigationGraph.route) {
        composable(route = Screen.CatalogNavigationGraph.Catalog.route) { navBackStackEntry ->
            Text(text = "Каталог")
        }
    }
}