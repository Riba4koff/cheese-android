package ru.antares.cheese_android.data.repository.auth.models

data class SessionModel(
    val authorizationType: String,
    val authorizedObject: String,
    val device: DeviceModel,
    val deviceId: String,
    val start: String,
    val finish: String,
    val id: String,
    val opened: Boolean,
)