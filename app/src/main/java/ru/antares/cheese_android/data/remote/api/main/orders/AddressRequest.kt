package ru.antares.cheese_android.data.remote.api.main.orders

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:21
 * Android Studio
 */

data class AddressRequest(
    val city: String,
    val street: String,
    val house: String,
    val building: String? = null,
    val floor: String? = null,
    val apartment: String? = null,
)
