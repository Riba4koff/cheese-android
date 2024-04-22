package ru.antares.cheese_android.data.remote.api.main.products

import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:27
 * Android Studio
 */

sealed interface ProductsError: CheeseError {
    data object ServerError : ProductsError
    data object UnknownError : ProductsError
    data object NoInternetError : ProductsError
    data object ReceiveError : ProductsError
}