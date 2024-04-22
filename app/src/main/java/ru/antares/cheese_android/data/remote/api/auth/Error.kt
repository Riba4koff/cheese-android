package ru.antares.cheese_android.data.remote.api.auth

import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:59
 * Android Studio
 */

sealed interface AuthorizationError: CheeseError {
    data object ServerError: AuthorizationError
    data object NoInternetConnectionError: AuthorizationError
    data object UnknownError : AuthorizationError
    data object LoadDataError: AuthorizationError
}