package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.homeNavigationGraph(
    navController: NavController,
) {
    navigation(
        route = Screen.HomeNavigationGraph.route,
        startDestination = Screen.HomeNavigationGraph.Home.route
    ) {
        composable(route = Screen.HomeNavigationGraph.Home.route) { navBackStackEntry ->
            Text(text = "Главная")
        }
    }
}