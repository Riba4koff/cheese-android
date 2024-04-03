package ru.antares.cheese_android.data.remote.services.main.profile

import ru.antares.cheese_android.data.remote.services.cart.CartError
import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:15
 * Android Studio
 */

sealed interface ProfileError : CheeseError {
    data object ServerError : ProfileError
    data object UnknownError : ProfileError
    data object NoInternetError : ProfileError
    data object ReceiveError : ProfileError
    data object UpdateError : ProfileError
    data object DeleteError : ProfileError
}