package ru.antares.cheese_android.data.remote.services.community.dto

import ru.antares.cheese_android.domain.models.community.ActivityModel

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:02
 * Android Studio
 */

data class ActivityDTO(
    val id: String,
    val event: EventDTO,
    val startFrom: String,
    val longitude: Double,
    val latitude: Double,
    val address: String,
    val addressDescription: String,
    val ticketPrice: Double,
    val amountOfTicket: Int,
    val ticketsLeft: Int
) {
    fun toModel() = ActivityModel(
        id = id,
        event = event.toModel(),
        startFrom = startFrom,
        longitude = longitude,
        latitude = latitude,
        address = address,
        addressDescription = addressDescription,
        ticketPrice = ticketPrice,
        amountOfTicket = amountOfTicket,
        ticketsLeft = ticketsLeft
    )
}