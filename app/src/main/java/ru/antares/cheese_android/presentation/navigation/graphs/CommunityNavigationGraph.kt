package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityScreen
import ru.antares.cheese_android.presentation.view.main.community_graph.community.CommunityViewModel

fun NavGraphBuilder.communityNavigationGraph(communityNavController: NavController) {
    navigation(
        route = Screen.CommunityNavigationGraph.route,
        startDestination = Screen.CommunityNavigationGraph.Community.route
    ) {
        composable(route = Screen.CommunityNavigationGraph.Community.route) { navBackStackEntry ->
            val viewModel: CommunityViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            CommunityScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }
    }
}