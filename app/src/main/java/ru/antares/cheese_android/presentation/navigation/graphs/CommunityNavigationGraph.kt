package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityScreen

fun NavGraphBuilder.communityNavigationGraph(communityNavController: NavController) {
    navigation(
        route = Screen.CommunityNavigationGraph.route,
        startDestination = Screen.CommunityNavigationGraph.Community.route
    ) {
        composable(route = Screen.CommunityNavigationGraph.Community.route) { navBackStackEntry ->
            CommunityScreen()
        }
    }
}