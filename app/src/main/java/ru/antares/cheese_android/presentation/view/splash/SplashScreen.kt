package ru.antares.cheese_android.presentation.view.splash

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.splash.SplashScreenState.*

@Composable
fun SplashScreen(
    navController: NavController,
    splashScreenState: SplashScreenState,
) {
    LaunchedEffect(splashScreenState) {
        when (splashScreenState) {
            LOADING -> {
                /*ANIMATION*/
            }

            NAVIGATE_TO_AUTHORIZE -> {
                navController.navigate(Screen.AuthNavigationGraph.route) {
                    popUpTo(Screen.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }

            NAVIGATE_TO_HOME_SCREEN -> {
                navController.navigate(Screen.HomeNavigationGraph.route) {
                    popUpTo(Screen.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}