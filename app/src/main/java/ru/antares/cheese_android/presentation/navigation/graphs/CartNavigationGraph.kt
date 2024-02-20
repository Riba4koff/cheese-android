package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartScreen

fun NavGraphBuilder.cartNavigationGraph(cartNavController: NavController) {
    navigation(
        route = Screen.CartNavigationGraph.route,
        startDestination = Screen.CartNavigationGraph.Cart.route
    ) {
        composable(route = Screen.CartNavigationGraph.Cart.route) { navBackStackEntry ->
            CartScreen()
        }
    }
}