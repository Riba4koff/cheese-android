package ru.antares.cheese_android.data.remote.services.cart

import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:07
 * Android Studio
 */

sealed interface CartError : CheeseError {
    data object ServerError : CartError
    data object UnknownError : CartError
    data object NoInternetError : CartError
    data object ReceiveError: CartError
    data object UpdateError : CartError
    data object ClearError : CartError
    data object DeleteProductError : CartError
}