package ru.antares.cheese_android.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val data: T? = null,
    val message: String
)
