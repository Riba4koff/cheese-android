package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ru.antares.cheese_android.presentation.navigation.navhosts.BottomBarNavHost
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.bottomBarNavigationGraph(globalNavController: NavHostController) {
    composable(
        enterTransition = {
            fadeIn(tween(128))
        },
        exitTransition = {
            fadeOut(tween(128))
        },
        route = Screen.HomeNavigationGraph.route
    ) {
        BottomBarNavHost(globalNavController = globalNavController)
    }
}