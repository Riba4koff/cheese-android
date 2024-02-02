package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.antares.cheese_android.presentation.navigation.graphs.profileNavigationGraph
import ru.antares.cheese_android.presentation.navigation.util.Screen

@Composable
fun ProfileNavHost(paddings: PaddingValues) {
    val profileNavController = rememberNavController()

    NavHost(
        modifier = Modifier.padding(paddings),
        navController = profileNavController,
        startDestination = Screen.ProfileNavigationGraph.route
    ) {
        profileNavigationGraph(profileNavController)
    }
}