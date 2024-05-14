package ru.antares.cheese_android.data.remote.api.main.orders

import ru.antares.cheese_android.data.remote.api.main.cart.CartProductDTO
import ru.antares.cheese_android.data.remote.api.main.profile.response.ProfileResponse
import ru.antares.cheese_android.domain.models.LastStatus
import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:23
 * Android Studio
 */

data class OrderDTO(
    val id: String,
    val user: ProfileResponse,
    val userNumber: ULong,
    val serialNumber: Int,
    val timestamp: String,
    val totalCost: Double,
    val deleted: Boolean,
    val address: AddressModel,
    val products: List<CartProductDTO>,
    val lastStatus: LastStatus? = null,
    val paymentType: PaymentType,
    val closed: String?
)