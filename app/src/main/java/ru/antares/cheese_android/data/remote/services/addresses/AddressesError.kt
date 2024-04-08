package ru.antares.cheese_android.data.remote.services.addresses

import ru.antares.cheese_android.data.remote.services.cart.CartError
import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 15:17
 * Android Studio
 */

sealed interface AddressesError : CheeseError {
    data object ServerError : AddressesError
    data object UnknownError : AddressesError
    data object NoInternetError : AddressesError
    data object CreateError: AddressesError
    data object ReceiveError: AddressesError
    data object DeleteError: AddressesError
    data object UpdateError : AddressesError
}