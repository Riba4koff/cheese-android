package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.communityNavigationGraph(communityNavController: NavController) {
    navigation(
        route = Screen.CommunityNavigationGraph.route,
        startDestination = Screen.CommunityNavigationGraph.Community.route
    ) {
        composable(route = Screen.CommunityNavigationGraph.Community.route) { navBackStackEntry ->
            Text(text = "Сообщество")
        }
    }
}