package ru.antares.cheese_android.data.remote.api.main.catalog

import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:19
 * Android Studio
 */

sealed interface CatalogError: CheeseError{
    data object ServerError : CatalogError
    data object UnknownError : CatalogError
    data object NoInternetError : CatalogError
    data object ReceiveError : CatalogError
}