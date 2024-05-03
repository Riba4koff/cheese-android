package ru.antares.cheese_android.domain.models

import ru.antares.cheese_android.data.remote.api.main.profile.response.ProfileResponse
import ru.antares.cheese_android.domain.models.community.ActivityModel

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 14:30
 * Android Studio
 */

data class TicketModel(
    val id: String,
    val user: ProfileResponse,
    val activity: ActivityModel,
    val price: Double,
    val paymentStatus: String,
    val boughtAt: String
)