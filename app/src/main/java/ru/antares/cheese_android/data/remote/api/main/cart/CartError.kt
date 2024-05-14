package ru.antares.cheese_android.data.remote.api.main.cart

import ru.antares.cheese_android.domain.result.CheeseError

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:07
 * Android Studio
 */

sealed interface CartError : CheeseError {
    val message: String

    data class ServerError(
        override val message: String = "Произошла ошибка на сервере"
    ) : CartError

    data class Unauthorized(
        override val message: String = "Вы не авторизованы"
    ) : CartError

    data class UnknownError(
        override val message: String = "Произошла неизвестная ошибка"
    ) : CartError

    data class NoInternetError(
        override val message: String = "Нет подключения к интернету"
    ) : CartError

    data class ReceiveError(
        override val message: String = "Произошла ошибка при загрузке"
    ) : CartError

    data class UpdateError(
        override val message: String = "Не удалось обновить корзину"
    ) : CartError

    data class ClearError(
        override val message: String = "Не удалось очистить корзину"
    ) : CartError

    data class DeleteProductError(
        override val message: String = "Не удалось удалить продукт из корзины"
    ) : CartError
}