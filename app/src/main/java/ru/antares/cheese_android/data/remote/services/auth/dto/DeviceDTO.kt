package ru.antares.cheese_android.data.remote.services.auth.dto

import kotlinx.serialization.Serializable

data class DeviceDTO(
    val firebaseToken: String,
    val firmware: String,
    val id: String?,
    val version: String
)
