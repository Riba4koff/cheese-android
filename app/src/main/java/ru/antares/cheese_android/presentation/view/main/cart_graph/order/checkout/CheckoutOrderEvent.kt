package ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout

import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 18.03.2024 at 16:33
 * Android Studio
 */

sealed interface CheckoutOrderEvent {
    data class OnAddressChange(val addressID: String): CheckoutOrderEvent
    data class OnReceiverChange(val receiver: String): CheckoutOrderEvent
    data class OnPaymentMethodChange(val paymentMethod: PaymentType): CheckoutOrderEvent
    data class OnCommentChange(val comment: String): CheckoutOrderEvent
}