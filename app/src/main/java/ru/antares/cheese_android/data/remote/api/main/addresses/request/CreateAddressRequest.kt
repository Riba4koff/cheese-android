package ru.antares.cheese_android.data.remote.api.main.addresses.request

data class CreateAddressRequest(
    val city: String,
    val street: String,
    val house: String,
    val title: String?,
    val building: String? = null,
    val apartment: String? = null,
    val entrance: String? = null,
    val floor: String? = null,
    val isDeleted: String? = null,
)