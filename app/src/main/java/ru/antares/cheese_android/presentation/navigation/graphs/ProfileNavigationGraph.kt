package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.profileNavigationGraph(profileNavController: NavController) {
    navigation(
        route = Screen.ProfileNavigationGraph.route,
        startDestination = Screen.ProfileNavigationGraph.Profile.route
    ) {
        composable(route = Screen.ProfileNavigationGraph.Profile.route) { navBackStackEntry ->
            Text(text = "Профиль")
        }
    }
}