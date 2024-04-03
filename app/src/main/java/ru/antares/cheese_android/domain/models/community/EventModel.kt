package ru.antares.cheese_android.domain.models.community

import androidx.compose.runtime.Immutable

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:27
 * Android Studio
 */

@Immutable
data class EventModel(
    val id: String,
    val title: String,
    val description: String
)