package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeScreen
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeViewModel
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneScreen
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.authNavigationGraph(
    globalNavController: NavController
) {
    val animationDurationValue = 0
    navigation(
        startDestination = Screen.AuthNavigationGraph.InputPhone.route,
        route = Screen.AuthNavigationGraph.route
    ) {
        composable(
            enterTransition = {
                slideInHorizontally(animationSpec = tween(300)) { it }
            },
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(300)) { -it }
            }, route = Screen.AuthNavigationGraph.InputPhone.route
        ) { _ ->
            val viewModel: InputPhoneViewModel = koinViewModel()
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            InputPhoneScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigationEvents = viewModel.navigationEvents,
                navController = globalNavController,
                onError = viewModel::onError
            )
        }
        composable(
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300))
            },
            route = Screen.AuthNavigationGraph.ConfirmCode.url,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val phone = navBackStackEntry.arguments?.getString("phone")!!
            val viewModel: ConfirmCodeViewModel = koinViewModel(
                parameters = { parametersOf(phone) }
            )
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            ConfirmCodeScreen(
                navController = globalNavController,
                state = state,
                onEvent = viewModel::onEvent,
                navigationEvents = viewModel.navigationEvents,
                onError = viewModel::onError
            )
        }
    }
}