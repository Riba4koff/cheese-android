package ru.antares.cheese_android.domain.models.community

import androidx.compose.runtime.Immutable

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:27
 * Android Studio
 */

@Immutable
data class ActivityModel(
    val id: String,
    val event: EventModel,
    val startFrom: String,
    val longitude: Double,
    val latitude: Double,
    val address: String,
    val addressDescription: String,
    val ticketPrice: String,
    val amountOfTicket: Int,
    val ticketsLeft: Int
)