package ru.antares.cheese_android.data.remote.api.auth.request

import ru.antares.cheese_android.data.remote.api.auth.dto.DeviceDTO

data class SendCodeRequest(
    val code: Int,
    val device: DeviceDTO
)