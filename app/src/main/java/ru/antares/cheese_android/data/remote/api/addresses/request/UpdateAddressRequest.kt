package ru.antares.cheese_android.data.remote.api.addresses.request

data class UpdateAddressRequest(
    val city: String? = null,
    val house: String? = null,
    val street: String? = null,
    val title: String? = null,
    val apartment: String? = null,
    val building: String? = null,
    val entrance: String? = null,
    val floor: String? = null,
    val isDeleted: String? = null,
)