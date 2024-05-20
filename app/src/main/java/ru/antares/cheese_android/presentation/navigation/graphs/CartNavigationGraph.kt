package ru.antares.cheese_android.presentation.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartNavigationEvent
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartScreen
import ru.antares.cheese_android.presentation.view.main.cart_graph.cart.CartViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout.CheckoutOrderEvent
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout.CheckoutOrderNavigationEvent
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout.CheckoutOrderScreen
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout.CheckoutOrderViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm.ConfirmOrderNavigationEvent
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm.ConfirmOrderScreen
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm.ConfirmOrderViewModel
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address.SelectAddressNavigationEvent
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address.SelectAddressScreen
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address.SelectAddressViewModel
import ru.antares.cheese_android.sharedViewModel

fun NavGraphBuilder.cartNavigationGraph(cartNavController: NavController) {
    navigation(
        route = Screen.CartNavigationGraph.route,
        startDestination = Screen.CartNavigationGraph.Cart.route
    ) {
        composable(
            route = Screen.CartNavigationGraph.Cart.route,
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
        ) { navBackStackEntry ->
            val viewModel: CartViewModel =
                navBackStackEntry.sharedViewModel(navController = cartNavController)
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    is CartNavigationEvent.ToCheckoutOrder -> {
                        cartNavController.navigate(Screen.CartNavigationGraph.CheckoutOrder.route + "/${navigationEvent.totalCost}")
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

        composable(
            route = Screen.CartNavigationGraph.CheckoutOrder.url,
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
            arguments = listOf(
                navArgument("total_cost") { type = NavType.FloatType }
            )
        ) { navBackStackEntry ->
            val viewModel: CheckoutOrderViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val totalCost = navBackStackEntry.arguments?.getFloat("total_cost") ?: 0f

            val lifeCycleOwner = LocalLifecycleOwner.current

            cartNavController
                .currentBackStackEntry
                ?.savedStateHandle
                ?.getLiveData<String>("address_id")
                ?.observe(LocalLifecycleOwner.current) { addressID ->
                    viewModel.onEvent(CheckoutOrderEvent.OnAddressChange(addressID = addressID))
                }

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    CheckoutOrderNavigationEvent.NavigateBack -> {
                        cartNavController.popBackStack()
                    }

                    is CheckoutOrderNavigationEvent.NavigateToConfirmOrder -> {
                        cartNavController.navigate(
                            Screen.CartNavigationGraph.ConfirmOrder.route +
                                    "/${navigationEvent.addressID}" +
                                    "/${navigationEvent.receiver.ifEmpty { "Не указан" }}" +
                                    "/${navigationEvent.paymentMethod.name}" +
                                    "/${navigationEvent.comment?.ifEmpty { "Не указан" }}" +
                                    "/${navigationEvent.totalCost}"
                        )
                    }

                    CheckoutOrderNavigationEvent.NavigateToSelectAddress -> {
                        cartNavController.navigate(Screen.CartNavigationGraph.SelectAddress.route)
                    }
                }
            }

            CheckoutOrderScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent,
                onError = viewModel::onError,
                totalCost = totalCost.toDouble()
            )
        }

        composable(
            route = Screen.CartNavigationGraph.ConfirmOrder.url,
            enterTransition = {
                fadeIn(tween(100))
            },
            exitTransition = {
                fadeOut(tween(100))
            },
            arguments = listOf(
                navArgument("address_id") { type = NavType.StringType },
                navArgument("receiver") { type = NavType.StringType },
                navArgument("payment_type") { type = NavType.StringType },
                navArgument("comment") { type = NavType.StringType },
                navArgument("total_cost") { type = NavType.FloatType },
            )
        ) { navBackStackEntry ->
            val viewModel: ConfirmOrderViewModel = koinViewModel(
                parameters = {
                    parametersOf(
                        navBackStackEntry.arguments?.getString("address_id"),
                        navBackStackEntry.arguments?.getString("receiver") ?: "null",
                        navBackStackEntry.arguments?.getString("payment_type"),
                        navBackStackEntry.arguments?.getString("comment") ?: "null",
                        navBackStackEntry.arguments?.getFloat("total_cost")
                    )
                }
            )
            val state by viewModel.state.collectAsStateWithLifecycle()

            ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvent ->
                when (navigationEvent) {
                    ConfirmOrderNavigationEvent.NavigateBack -> {
                        cartNavController.popBackStack()
                    }

                    ConfirmOrderNavigationEvent.NavigateToPay -> {
                        /* TODO: ... */
                    }
                }
            }

            ConfirmOrderScreen(
                state = state,
                onEvent = viewModel::onEvent,
                onNavigationEvent = viewModel::onNavigationEvent
            )
        }
    }

    composable(
        route = Screen.CartNavigationGraph.SelectAddress.url,
        enterTransition = {
            fadeIn(tween(100))
        },
        exitTransition = {
            fadeOut(tween(100))
        }
    ) { navBackStackEntry ->
        val viewModel: SelectAddressViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ObserveAsNavigationEvents(flow = viewModel.navigationEvents) { navigationEvents ->
            when (navigationEvents) {
                SelectAddressNavigationEvent.NavigateBack -> cartNavController.popBackStack()
                SelectAddressNavigationEvent.NavigateToAddAddress -> TODO()
                is SelectAddressNavigationEvent.NavigateToCheckoutOrder -> {
                    cartNavController.previousBackStackEntry?.savedStateHandle?.set("address_id", navigationEvents.id)
                    cartNavController.popBackStack()
                }
            }
        }

        SelectAddressScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onNavigationEvent = viewModel::onNavigationEvent
        )
    }
}