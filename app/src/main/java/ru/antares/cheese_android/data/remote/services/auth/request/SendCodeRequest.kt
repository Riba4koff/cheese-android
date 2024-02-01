package ru.antares.cheese_android.data.remote.services.auth.request

import kotlinx.serialization.Serializable
import ru.antares.cheese_android.data.remote.services.auth.dto.DeviceDTO

data class SendCodeRequest(
    val code: Int,
    val device: DeviceDTO
)