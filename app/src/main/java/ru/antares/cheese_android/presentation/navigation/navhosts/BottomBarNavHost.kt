package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val isAuthorized by mainViewModel.isAuthorized.collectAsStateWithLifecycle()

    val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    Scaffold(
        bottomBar = {
            BottomBar(
                countInCart = countInProductsInCart,
                navigate = { destination ->
                    bottomBarNavController.navigate(destination.route) {
                        bottomBarNavController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                currentRoute = currentRoute,
                isAuthorized = isAuthorized
            )
        },
    ) { paddings ->
        Box(modifier = Modifier.padding(paddings)) {
            NavHost(
                navController = bottomBarNavController,
                startDestination = Screen.HomeNavigationGraph.route
            ) {
                composable(
                    enterTransition = {
                        fadeIn(tween(128))
                    },
                    exitTransition = {
                        fadeOut(tween(128))
                    },
                    route = Screen.HomeNavigationGraph.route,
                ) { _ ->
                    val homeNavController = rememberNavController()

                    HomeNavHost(
                        homeNavController = homeNavController
                    )
                }
                composable(
                    enterTransition = {
                        fadeIn(tween(128))
                    },
                    exitTransition = {
                        fadeOut(tween(128))
                    },
                    route = Screen.CatalogNavigationGraph.route
                ) { _ ->
                    val catalogNavController = rememberNavController()

                    CatalogNavHost(
                        catalogNavController = catalogNavController
                    )
                }
                composable(
                    enterTransition = {
                        fadeIn(tween(128))
                    }, exitTransition = {
                        fadeOut(tween(128))
                    },
                    route = Screen.CommunityNavigationGraph.route
                ) { _ ->
                    val communityNavController = rememberNavController()

                    CommunityNavHost(
                        communityNavController = communityNavController
                    )
                }
                composable(
                    enterTransition = {
                        fadeIn(tween(128))
                    },
                    exitTransition = {
                        fadeOut(tween(128))
                    },
                    route = Screen.CartNavigationGraph.route
                ) { _ ->
                    val cartNavController = rememberNavController()

                    CartNavHost(
                        cartNavController = cartNavController
                    )
                }
                composable(
                    enterTransition = {
                        fadeIn(tween(128))
                    },
                    exitTransition = {
                        fadeOut(tween(128))
                    },
                    route = Screen.ProfileNavigationGraph.route
                ) { _ ->
                    val profileNavController = rememberNavController()

                    ProfileNavHost(
                        globalNavController = globalNavController,
                        profileNavController = profileNavController
                    )
                }
            }
        }
    }
}