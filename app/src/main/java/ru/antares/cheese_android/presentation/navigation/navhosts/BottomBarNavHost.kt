package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import ru.antares.cheese_android.presentation.navigation.bottomBar.BottomBar
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.MainViewModel

@Composable
fun BottomBarNavHost(globalNavController: NavHostController) {
    val bottomBarNavController = rememberNavController()
    val mainViewModel: MainViewModel = koinViewModel()
    val countInProductsInCart by mainViewModel.countProductsInCart.collectAsStateWithLifecycle()

    Scaffold(bottomBar = {
        BottomBar(navController = bottomBarNavController, countInCart = countInProductsInCart)
    }) { paddings ->
        NavHost(
            navController = bottomBarNavController,
            startDestination = Screen.HomeNavigationGraph.route
        ) {
            composable(
                enterTransition = {
                    fadeIn(tween(100))
                },
                exitTransition = {
                    fadeOut(tween(100))
                },
                route = Screen.HomeNavigationGraph.route,
            ) { _ ->
                HomeNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(100))
                },
                exitTransition = {
                    fadeOut(tween(100))
                },
                route = Screen.CatalogNavigationGraph.route
            ) { _ ->
                CatalogNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(100))
                }, exitTransition = {
                    fadeOut(tween(100))
                },
                route = Screen.CommunityNavigationGraph.route
            ) { _ ->
                CommunityNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(100))
                },
                exitTransition = {
                    fadeOut(tween(100))
                },
                route = Screen.CartNavigationGraph.route
            ) { _ ->
                CartNavHost(paddings = paddings)
            }
            composable(
                enterTransition = {
                    fadeIn(tween(100))
                },
                exitTransition = {
                    fadeOut(tween(100))
                },
                route = Screen.ProfileNavigationGraph.route
            ) { _ ->
                ProfileNavHost(paddings = paddings, globalNavController = globalNavController)
            }
        }
    }
}