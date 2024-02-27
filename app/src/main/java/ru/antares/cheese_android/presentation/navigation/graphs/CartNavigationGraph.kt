package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartNavigationEvent
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartScreen
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.cartNavigationGraph(cartNavController: NavController) {
    navigation(
        route = Screen.CartNavigationGraph.route,
        startDestination = Screen.CartNavigationGraph.Cart.route
    ) {
        composable(route = Screen.CartNavigationGraph.Cart.route) { navBackStackEntry ->
            val viewModel: CartViewModel = navBackStackEntry.sharedViewModel(navController = cartNavController)
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    CartNavigationEvent.ToCheckoutOrder -> {
                        /* TODO: navigate to checkout order */
                    }

                    CartNavigationEvent.NavigateToCatalog -> {

                    }
                }
            }

            CartScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onError = viewModel::onError,
                onNavigationEvent = viewModel::onNavigationEvent,
            )
        }
    }
}