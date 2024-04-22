package ru.antares.cheese_android.data.remote.api.auth.dto

data class DeviceDTO(
    val firebaseToken: String,
    val firmware: String,
    val id: String?,
    val version: String
)
