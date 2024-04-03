package ru.antares.cheese_android.data.remote.models

data class Pagination<T>(
    val result: List<T>,
    val sizeResult: Int,
    val page: Int,
    val amountOfPages: Int,
    val amountOfAll: Int,
)

fun <A, B> Pagination<A>.map(block: (A) -> B) = Pagination(
    result = result.map {
        block(it)
    },
    sizeResult = sizeResult,
    page = page,
    amountOfPages = amountOfPages,
    amountOfAll = amountOfAll
)