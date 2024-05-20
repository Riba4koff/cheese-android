package ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address

import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 20.05.2024 at 14:45
 * Android Studio
 */

sealed interface SelectAddressNavigationEvent {
    data object NavigateBack: SelectAddressNavigationEvent
    data class NavigateToCheckoutOrder(val id: String): SelectAddressNavigationEvent
    data object NavigateToAddAddress: SelectAddressNavigationEvent
}