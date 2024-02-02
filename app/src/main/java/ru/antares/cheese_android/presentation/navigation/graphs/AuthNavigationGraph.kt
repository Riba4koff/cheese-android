package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.authNavigationGraph(
    navController: NavController
) {
    navigation(startDestination = Screen.AuthNavigationGraph.InputPhone.route, route = Screen.AuthNavigationGraph.route) {
        composable(route = Screen.AuthNavigationGraph.InputPhone.route) { navBackStackEntry ->
            Text("Input Phone")
        }
        composable(route = Screen.AuthNavigationGraph.ConfirmCode.route) { navBackStackEntry ->
            Text("Confirm Code")
        }
    }
}