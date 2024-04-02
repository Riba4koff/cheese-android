package ru.antares.cheese_android.data.remote.dto

data class AddressDTO(
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
)