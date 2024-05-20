package ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address

import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 20.05.2024 at 14:46
 * Android Studio
 */

sealed interface SelectAddressEvent {
    data class SetAddress(val address: AddressModel) : SelectAddressEvent
    data class LoadNextPage(val page: Int, val size: Int) : SelectAddressEvent
}