package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.antares.cheese_android.presentation.navigation.graphs.catalogNavigationGraph
import ru.antares.cheese_android.presentation.navigation.util.Screen

@Composable
fun CatalogNavHost(
    catalogNavController: NavHostController
) {
    val navBackStackEntry by catalogNavController.currentBackStackEntryAsState()
    val nextRoute = navBackStackEntry?.destination?.route

    NavHost(
        navController = catalogNavController,
        startDestination = Screen.CatalogNavigationGraph.route
    ) {
        catalogNavigationGraph(
            catalogNavController = catalogNavController,
            nextRoute = nextRoute,
        )
    }
}