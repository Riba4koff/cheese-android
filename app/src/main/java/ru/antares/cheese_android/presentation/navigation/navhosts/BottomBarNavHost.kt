package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.antares.cheese_android.presentation.navigation.bottomBar.BottomBar
import ru.antares.cheese_android.presentation.navigation.util.Screen

@Composable
fun BottomBarNavHost(globalNavController: NavHostController) {
    val bottomBarNavController = rememberNavController()

    Scaffold(bottomBar = {
        BottomBar(navController = bottomBarNavController, countInCart = 1)
    }) { paddings ->
        NavHost(
            navController = bottomBarNavController,
            startDestination = Screen.HomeNavigationGraph.route
        ) {
            composable(
                enterTransition = {
                    fadeIn(tween(0))
                },
                exitTransition = {
                    fadeOut(tween(0))
                },
                route = Screen.HomeNavigationGraph.route,
            ) { _ ->
                HomeNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(0))
                },
                exitTransition = {
                    fadeOut(tween(0))
                },
                route = Screen.CatalogNavigationGraph.route
            ) { _ ->
                CatalogNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(0))
                }, exitTransition = {
                    fadeOut(tween(0))
                },
                route = Screen.CommunityNavigationGraph.route
            ) { _ ->
                CommunityNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(0))
                },
                exitTransition = {
                    fadeOut(tween(0))
                },
                route = Screen.CartNavigationGraph.route
            ) { _ ->
                CartNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(0))
                },
                exitTransition = {
                    fadeOut(tween(0))
                },
                route = Screen.ProfileNavigationGraph.route
            ) { _ ->
                ProfileNavHost(paddings = paddings, globalNavController = globalNavController)
            }
        }
    }
}