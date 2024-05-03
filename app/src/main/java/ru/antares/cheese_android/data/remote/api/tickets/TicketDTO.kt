package ru.antares.cheese_android.data.remote.api.tickets

import ru.antares.cheese_android.data.remote.api.community.dto.ActivityDTO
import ru.antares.cheese_android.data.remote.api.main.profile.response.ProfileResponse
import ru.antares.cheese_android.domain.models.TicketModel

data class TicketDTO(
    val id: String,
    val user: ProfileResponse,
    val activity: ActivityDTO,
    val price: Double,
    val paymentStatus: String,
    val boughtAt: String
) {
    fun toModel() = TicketModel(
        id = id,
        user = user,
        activity = activity.toModel(),
        price = price,
        paymentStatus = paymentStatus,
        boughtAt = boughtAt
    )
}
