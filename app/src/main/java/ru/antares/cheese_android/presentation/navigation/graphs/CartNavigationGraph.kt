package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen

fun NavGraphBuilder.cartNavigationGraph(cartNavController: NavController) {
    navigation(
        route = Screen.CartNavigationGraph.route,
        startDestination = Screen.CartNavigationGraph.Cart.route
    ) {
        composable(route = Screen.CartNavigationGraph.Cart.route) { navBackStackEntry ->
            Text(text = "Корзина")
        }
    }
}