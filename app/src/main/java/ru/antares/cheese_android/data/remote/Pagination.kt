package ru.antares.cheese_android.data.remote

data class Pagination<T>(
    val result: List<T>,
    val sizeResult: Int,
    val page: Int,
    val amountOfPages: Int,
    val amountOfAll: Int,
)
