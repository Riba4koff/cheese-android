package ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout

import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 18.03.2024 at 16:34
 * Android Studio
 */

sealed interface CheckoutOrderNavigationEvent {
    data class NavigateToConfirmOrder(
        val addressID: String,
        val receiver: String,
        val paymentMethod: PaymentType,
        val totalCost: Double,
        val comment: String? = null,
    ) : CheckoutOrderNavigationEvent

    data object NavigateBack: CheckoutOrderNavigationEvent
    data object NavigateToSelectAddress : CheckoutOrderNavigationEvent
}