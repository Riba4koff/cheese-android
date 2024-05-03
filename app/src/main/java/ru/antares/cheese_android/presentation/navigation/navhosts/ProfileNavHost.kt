package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ru.antares.cheese_android.presentation.navigation.graphs.profileNavigationGraph
import ru.antares.cheese_android.presentation.navigation.util.Screen

@Composable
fun ProfileNavHost(
    globalNavController: NavController,
    profileNavController: NavHostController
) {
    NavHost(
        navController = profileNavController,
        startDestination = Screen.ProfileNavigationGraph.route
    ) {
        profileNavigationGraph(profileNavController, globalNavController)
    }
}