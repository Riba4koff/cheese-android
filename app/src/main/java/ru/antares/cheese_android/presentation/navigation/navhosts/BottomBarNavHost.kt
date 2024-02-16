package ru.antares.cheese_android.presentation.navigation.navhosts

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

    Scaffold(
        bottomBar = {
            BottomBar(navController = bottomBarNavController, countInCart = 1)
        }
    ) { paddings ->
        NavHost(navController = bottomBarNavController, startDestination = Screen.HomeNavigationGraph.route) {
            composable(route = Screen.HomeNavigationGraph.route) { _ ->
                HomeNavHost(paddings = paddings)
            }
            composable(route = Screen.CatalogNavigationGraph.route) { _ ->
                CatalogNavHost(paddings = paddings)
            }
            composable(route = Screen.CommunityNavigationGraph.route) { _ ->
                CommunityNavHost(paddings = paddings)
            }
            composable(route = Screen.CartNavigationGraph.route) { _ ->
                CartNavHost(paddings = paddings)
            }
            composable(route = Screen.ProfileNavigationGraph.route) { _ ->
                ProfileNavHost(paddings = paddings, globalNavController = globalNavController)
            }
        }
    }
}