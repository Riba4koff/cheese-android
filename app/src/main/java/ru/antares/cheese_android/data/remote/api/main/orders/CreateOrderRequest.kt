package ru.antares.cheese_android.data.remote.api.main.orders

import ru.antares.cheese_android.domain.paymentType.PaymentType

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:20
 * Android Studio
 */

data class CreateOrderRequest(
    val address: AddressRequest,
    val recipient: String? = null,
    val paymentType: PaymentType,
    val comment: String
)