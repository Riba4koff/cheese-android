package ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm

import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 29.03.2024 at 15:57
 * Android Studio
 */

@Immutable
data class ConfirmOrderState(
    val loading: Boolean = false,
    val address: AddressModel? = null,
    val receiver: String? = null,
    val products: List<CartProductModel>? = null,
    val paymentMethod: PaymentType? = null,
    val comment: String? = null,
    val totalCost: Double? = null
)