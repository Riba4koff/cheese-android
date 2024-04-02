package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.splash.SplashScreen
import ru.antares.cheese_android.presentation.view.splash.SplashScreenViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.splashScreen(navController: NavController) {
    composable(
        exitTransition = {
            fadeOut(tween(100))
        },
        route = Screen.SplashScreen.route
    ) { navBackStackEntry ->
        val viewModel: SplashScreenViewModel =
            navBackStackEntry.sharedViewModel(navController = navController)
        val splashScreenState by viewModel.userAuthorizedState.collectAsStateWithLifecycle()

        SplashScreen(navController = navController, splashScreenState = splashScreenState)
    }
}