package ru.antares.cheese_android.data.remote.api.tickets

import ru.antares.cheese_android.data.remote.api.cart.CartError
import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 22.04.2024 at 18:55
 * Android Studio
 */

sealed interface TicketsError: CheeseError {
    val message: String

    data class ServerError(
        override val message: String = "Произошла ошибка на сервере"
    ): TicketsError

    data class Unauthorized(
        override val message: String = "Вы не авторизованы"
    ) : TicketsError

    data class UnknownError(
        override val message: String = "Произошла неизвестная ошибка"
    ) : TicketsError

    data class NoInternetError(
        override val message: String = "Нет подключения к интернету"
    ) : TicketsError

    data class ReceiveError(
        override val message: String = "Произошла ошибка при загрузке"
    ) : TicketsError
}