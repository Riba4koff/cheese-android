package ru.antares.cheese_android.domain.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

sealed class Animations(
    val enter: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition),
    val exit: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition),
) {
    companion object {
        const val ANIMATE_TIME = 200
    }

    data object Default : Animations(
        enter = {
            fadeIn(animationSpec = tween(ANIMATE_TIME, easing = LinearEasing))
        },
        exit = {
            fadeOut(animationSpec = tween(ANIMATE_TIME, easing = LinearEasing))
        }
    )

    data object AnimateToRight : Animations(
        enter = {
            slideIntoContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
            )
        },
        exit = {
            slideOutOfContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Left
            )
        }
    )

    data object AnimateToUp : Animations(
        enter = {
            slideIntoContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Down
            )
        },
        exit = {
            slideOutOfContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Up
            )
        }
    )

    data object AnimateToLeft : Animations(
        enter = {
            slideIntoContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Left
            )
        },
        exit = {
            slideOutOfContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Right
            )
        }
    )

    data object AnimateToDown : Animations(
        enter = {
            slideIntoContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Up
            )
        },
        exit = {
            slideOutOfContainer(
                animationSpec = tween(ANIMATE_TIME, easing = EaseIn),
                towards = AnimatedContentTransitionScope.SlideDirection.Down
            )
        }
    )
}