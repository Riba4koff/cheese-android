package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.antares.cheese_android.presentation.navigation.graphs.communityNavigationGraph
import ru.antares.cheese_android.presentation.navigation.util.Screen

@Composable
fun CommunityNavHost(
    paddings: PaddingValues,
    communityNavController: NavHostController
) {
    NavHost(
        modifier = Modifier.padding(paddings),
        navController = communityNavController,
        startDestination = Screen.CommunityNavigationGraph.route
    ) {
        communityNavigationGraph(communityNavController)
    }
}

