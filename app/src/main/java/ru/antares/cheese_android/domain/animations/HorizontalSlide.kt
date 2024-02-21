package ru.antares.cheese_android.domain.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

/**
 * HorizontalSlide.kt
 * @author Павел
 * Created by on 21.02.2024 at 23:23
 * Android studio
 */

/**
 * Horizontal screen animation function (ENTER)
 *
 * navigation: screen_1 -> screen_2 -> screen_3
 *
 * for screen_2 - screen_1 is [previousRoute]
 *
 * for screen_3 - screen_2 is [previousRoute]
 * */
fun enterHorizontallySlide(previousRoute: String): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) {
    return {
        val isPreviousScreen = previousRoute == initialState.destination.route

        val towards = if (isPreviousScreen) AnimatedContentTransitionScope.SlideDirection.Right
        else AnimatedContentTransitionScope.SlideDirection.Left

        slideIntoContainer(
            animationSpec = tween(Animations.ANIMATE_TIME, easing = EaseIn),
            towards = towards
        )
    }
}

/**
 * Horizontal screen animation function (EXIT)
 *
 * val navController = rememberNavController()
 *
 * val navBackStackEntry by catalogNavController.currentBackStackEntryAsState()
 *
 * val nextRoute = navBackStackEntry?.destination?.route
 *
 * navigation: screen_1 -> screen_2
 *
 * for screen_1, condition: <nextRoute == screen_2> -> true
 *
 * for screen_2, condition: <nextRoute == screen_1> -> false
 * */
fun exitHorizontallySlide(isNextRoute: Boolean): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) {
    return {
        val towards = if (isNextRoute) AnimatedContentTransitionScope.SlideDirection.Left
        else AnimatedContentTransitionScope.SlideDirection.Right

        slideOutOfContainer(
            animationSpec = tween(Animations.ANIMATE_TIME, easing = EaseIn),
            towards = towards
        )
    }
}