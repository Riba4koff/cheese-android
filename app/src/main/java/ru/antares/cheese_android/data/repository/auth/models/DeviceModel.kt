package ru.antares.cheese_android.data.repository.auth.models

data class DeviceModel(
    val firebaseToken: String,
    val firmware: String,
    val id: String?,
    val version: String
)
