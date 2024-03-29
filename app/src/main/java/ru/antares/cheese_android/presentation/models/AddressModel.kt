package ru.antares.cheese_android.presentation.models

import ru.antares.cheese_android.data.remote.dto.AddressDTO

data class AddressModel(
    val id: String,
    val userID: String,
    val city: String,
    val street: String,
    val house: String,
    val title: String,
    val apartment: String? = null,
    val building: String? = null,
    val entrance: String? = null,
    val floor: String? = null,
    val isDeleted: Boolean? = null
) {
    fun get() = buildString {
        append("$city, $street, $house")
        building?.let { append("/$building") }
        apartment?.let { append(", $apartment") }
        entrance?.let { append(", $entrance") }
        floor?.let { append(", $floor") }
    }
}