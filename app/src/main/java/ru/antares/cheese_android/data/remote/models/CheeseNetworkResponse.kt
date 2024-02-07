package ru.antares.cheese_android.data.remote.models

data class CheeseNetworkResponse<T>(
    val data: T? = null,
    val message: String = ""
)
