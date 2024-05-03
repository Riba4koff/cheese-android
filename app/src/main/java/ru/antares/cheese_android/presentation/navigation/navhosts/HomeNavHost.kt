package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.navigation.graphs.homeNavigationGraph

@Composable
fun HomeNavHost(
    homeNavController: NavHostController,
) {
    NavHost(
        navController = homeNavController,
        startDestination = Screen.HomeNavigationGraph.route
    ) {
        homeNavigationGraph(homeNavController)
    }
}