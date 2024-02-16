package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.antares.cheese_android.presentation.navigation.navhosts.BottomBarNavHost
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.bottomBarNavigationGraph(globalNavController: NavHostController) {
    composable(route = Screen.HomeNavigationGraph.route) {
        BottomBarNavHost(globalNavController = globalNavController)
    }
}