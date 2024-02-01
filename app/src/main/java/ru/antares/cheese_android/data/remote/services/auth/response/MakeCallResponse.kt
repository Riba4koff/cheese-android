package ru.antares.cheese_android.data.remote.services.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class MakeCallResponse(
    val data: Boolean
)