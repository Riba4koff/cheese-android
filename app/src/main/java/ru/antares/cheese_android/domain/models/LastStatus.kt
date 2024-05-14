package ru.antares.cheese_android.domain.models

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:35
 * Android Studio
 */

data class LastStatus(
    val id: String,
    val name: String,
    val cancelable: Boolean,
    val color: String,
    val timestamp: String
)