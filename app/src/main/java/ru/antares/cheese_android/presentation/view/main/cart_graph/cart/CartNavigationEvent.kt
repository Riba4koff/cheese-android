package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:53
 * Android Studio
 */

sealed interface CartNavigationEvent {
    data class ToCheckoutOrder(val totalCost: Double): CartNavigationEvent
    data object NavigateToCatalog : CartNavigationEvent
}