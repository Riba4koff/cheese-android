package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.getViewModel
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataScreen
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileScreen
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.profileNavigationGraph(
    profileNavController: NavController,
    globalNavController: NavController
) {
    navigation(
        route = Screen.ProfileNavigationGraph.route,
        startDestination = Screen.ProfileNavigationGraph.Profile.route
    ) {
        composable(route = Screen.ProfileNavigationGraph.Profile.route) { navBackStackEntry ->
            val viewModel: ProfileViewModel =
                navBackStackEntry.sharedViewModel(navController = profileNavController)
            val state by viewModel.state.collectAsStateWithLifecycle()

            ProfileScreen(
                state = state,
                navigationEvents = viewModel.navigationEvents,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent,
                globalNavController = globalNavController,
                profileNavController = profileNavController,
                onError = viewModel::onError
            )
        }
        composable(route = Screen.ProfileNavigationGraph.PersonalData.route) { _ ->
            val viewModel: PersonalDataViewModel = getViewModel()
                /*navBackStackEntry.sharedViewModel(navController = profileNavController)*/
            val state by viewModel.state.collectAsStateWithLifecycle()

            PersonalDataScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onError = viewModel::onError,
                navController = profileNavController,
                navigationEvents = viewModel.navigationEvents
            )
        }
        composable(route = Screen.ProfileNavigationGraph.Addresses.route) { navBackStackEntry ->
            Text(text = "Адреса")
        }
        composable(route = Screen.ProfileNavigationGraph.Orders.route) { navBackStackEntry ->
            Text(text = "Заказы")
        }
        composable(route = Screen.ProfileNavigationGraph.Tickets.route) { navBackStackEntry ->
            Text(text = "Мои билеты")
        }
        composable(route = Screen.ProfileNavigationGraph.AboutApp.route) { navBackStackEntry ->
            Text(text = "О приложении")
        }
    }
}