package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreen

fun NavGraphBuilder.homeNavigationGraph(
    navController: NavController,
) {
    navigation(
        route = Screen.HomeNavigationGraph.route,
        startDestination = Screen.HomeNavigationGraph.Home.route
    ) {
        composable(route = Screen.HomeNavigationGraph.Home.route) { navBackStackEntry ->
            HomeScreen()
        }
    }
}