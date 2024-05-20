package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.domain.animations.Animations
import ru.antares.cheese_android.domain.animations.enterHorizontallySlide
import ru.antares.cheese_android.domain.animations.exitHorizontallySlide
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.profile_graph.about_app.AboutAppScreen
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.AddressesNavigationEvent
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.AddressesScreen
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.AddressesViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create.CreateAddressNavigationEvent
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create.CreateAddressScreen
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create.CreateAddressViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataScreen
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileScreen
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.profileNavigationGraph(
    profileNavController: NavController,
    globalNavController: NavController,
    nextRoute: String?
) {
    navigation(
        route = Screen.ProfileNavigationGraph.route,
        startDestination = Screen.ProfileNavigationGraph.Profile.route
    ) {
        composable(
            enterTransition = Animations.AnimateToRight.enter,
            exitTransition = Animations.AnimateToRight.exit,
            route = Screen.ProfileNavigationGraph.Profile.route,
        ) { navBackStackEntry ->
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
        composable(
            enterTransition = Animations.AnimateToLeft.enter,
            exitTransition = Animations.AnimateToLeft.exit,
            route = Screen.ProfileNavigationGraph.PersonalData.route
        ) { _ ->
            val viewModel: PersonalDataViewModel = getViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            PersonalDataScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onError = viewModel::onError,
                navController = profileNavController,
                navigationEvents = viewModel.navigationEvents
            )
        }
        composable(
            route = Screen.ProfileNavigationGraph.Addresses.route,
            enterTransition = enterHorizontallySlide(Screen.ProfileNavigationGraph.CreateAddress.route),
            exitTransition = exitHorizontallySlide(Screen.ProfileNavigationGraph.CreateAddress.route == nextRoute)
        ) { _ ->
            val viewModel: AddressesViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(
                flow = viewModel.navigationEvents,
            ) { navigationEvent ->
                when (navigationEvent) {
                    AddressesNavigationEvent.NavigateBack -> {
                        profileNavController.popBackStack()
                    }

                    AddressesNavigationEvent.NavigateToAddAddress -> {
                        profileNavController.navigate(Screen.ProfileNavigationGraph.CreateAddress.route)
                    }
                }
            }

            AddressesScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }
        composable(
            route = Screen.ProfileNavigationGraph.CreateAddress.route,
            enterTransition = Animations.AnimateToLeft.enter,
            exitTransition = Animations.AnimateToLeft.exit
        ) {  _ ->
            val viewModel: CreateAddressViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    CreateAddressNavigationEvent.NavigateBack -> {
                        profileNavController.popBackStack()
                    }
                }
            }

            CreateAddressScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }
        composable(
            route = Screen.ProfileNavigationGraph.Orders.route,
            enterTransition = Animations.AnimateToLeft.enter,
            exitTransition = Animations.AnimateToLeft.exit
        ) { navBackStackEntry ->
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Заказы")
            }
        }
        composable(
            route = Screen.ProfileNavigationGraph.Tickets.route,
            enterTransition = Animations.AnimateToLeft.enter,
            exitTransition = Animations.AnimateToLeft.exit
        ) { navBackStackEntry ->
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Мои билеты")
            }
        }
        composable(
            route = Screen.ProfileNavigationGraph.AboutApp.route,
            enterTransition = Animations.AnimateToLeft.enter,
            exitTransition = Animations.AnimateToLeft.exit
        ) { _ ->
            AboutAppScreen(navController = profileNavController)
        }
    }
}