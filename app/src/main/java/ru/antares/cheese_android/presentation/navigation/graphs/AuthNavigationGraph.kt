package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.core.parameter.parametersOf
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeScreen
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeViewModel
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneScreen
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.authNavigationGraph(
    navController: NavController
) {
    navigation(
        startDestination = Screen.AuthNavigationGraph.InputPhone.route,
        route = Screen.AuthNavigationGraph.route
    ) {
        composable(route = Screen.AuthNavigationGraph.InputPhone.route) { navBackStackEntry ->
            val viewModel: InputPhoneViewModel =
                navBackStackEntry.sharedViewModel(navController = navController)
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            InputPhoneScreen(
                state = state,
                onEvent = viewModel::onEvent,
                navigationEvents = viewModel.navigationActions,
                navController = navController
            )
        }
        composable(
            route = Screen.AuthNavigationGraph.ConfirmCode.url,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val phone = navBackStackEntry.arguments?.getString("phone")!!
            val viewModel: ConfirmCodeViewModel = navBackStackEntry.sharedViewModel(
                navController = navController,
                parameters = { parametersOf(phone) }
            )
            val state by viewModel.stateFlow.collectAsStateWithLifecycle()

            ConfirmCodeScreen(
                navController = navController,
                state = state,
                onEvent = viewModel::onEvent,
                navigationEvents = viewModel.navigationEvents
            )
        }
    }
}