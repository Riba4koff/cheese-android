package ru.antares.cheese_android.data.remote.api.main.orders

import kotlinx.datetime.LocalDateTime
import ru.antares.cheese_android.data.remote.api.main.cart.CartProductDTO
import ru.antares.cheese_android.data.remote.api.main.profile.response.ProfileResponse
import ru.antares.cheese_android.domain.models.LastStatus
import ru.antares.cheese_android.domain.paymentType.PaymentType

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:38
 * Android Studio
 */

data class CreateOrderResponse(
    val id: String,
    val user: ProfileResponse,
    val userNumber: ULong,
    val serialNumber: Int,
    val paymentType: PaymentType,
    val timestamp: LocalDateTime,
    val totalCost: Double,
    val address: AddressRequest,
    val comment: String?,
    val products: List<CartProductDTO>,
    val statuses: List<LastStatus>? = null
)