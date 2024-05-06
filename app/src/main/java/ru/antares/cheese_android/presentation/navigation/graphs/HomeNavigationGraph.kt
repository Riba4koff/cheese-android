package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreen
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreenNavigationEvent
import ru.antares.cheese_android.presentation.view.main.home_graph.home.HomeScreenViewModel

fun NavGraphBuilder.homeNavigationGraph(
    navController: NavController,
) {
    navigation(
        route = Screen.HomeNavigationGraph.route,
        startDestination = Screen.HomeNavigationGraph.Home.route
    ) {
        composable(
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
            route = Screen.HomeNavigationGraph.Home.route
        ) { navBackStackEntry ->
            val viewModel: HomeScreenViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    is HomeScreenNavigationEvent.NavigateToActivity -> {
                        /*TODO: navigate to activity*/
                    }
                    is HomeScreenNavigationEvent.NavigateToPost -> {
                        /*TODO: navigate to post*/
                    }
                    is HomeScreenNavigationEvent.NavigateToRecommendation -> {
                        /*TODO: navigate to recommendation*/
                    }
                }
            }

            HomeScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }
    }
}