package ru.antares.cheese_android.data.remote.api.main.community.dto

import ru.antares.cheese_android.domain.models.community.EventModel

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:02
 * Android Studio
 */

data class EventDTO(
    val id: String,
    val title: String,
    val description: String
)

fun EventDTO.toModel() = EventModel(
    id = id,
    title = title,
    description = description
)