package ru.antares.cheese_android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.navigation.graphs.authNavigationGraph
import ru.antares.cheese_android.presentation.navigation.graphs.bottomBarNavigationGraph
import ru.antares.cheese_android.presentation.navigation.graphs.splashScreen

@Composable
fun CheeseApp(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        splashScreen(navController)
        authNavigationGraph(navController)
        bottomBarNavigationGraph(navController)
    }
}



