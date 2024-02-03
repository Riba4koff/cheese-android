package ru.antares.cheese_android.data.remote.models

data class Response<T>(
    val data: T? = null,
    val message: String = ""
)
