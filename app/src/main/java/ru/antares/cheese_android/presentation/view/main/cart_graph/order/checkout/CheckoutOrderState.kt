package ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.presentation.models.AddressModel
import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * @author pavelrybakov
 * Created 18.03.2024 at 16:12
 * Android Studio
 */

@optics
@Immutable
data class CheckoutOrderState(
    val loading: Boolean = false,
    val error: AppError? = null,
    val totalCost: Double? = null,
    val paymentMethod: PaymentType? = null,
    val address: AddressModel? = null,
    val receiver: String = "",
    val comment: String = "",
    val products: List<CartProductModel> = emptyList()
) { companion object }