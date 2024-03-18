package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import ru.antares.cheese_android.domain.errors.AppError

/**
 * CartUIError.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:38
 * Android studio
 */

sealed interface CartAppError : AppError {
    data class LoadCartError(
        override val message: String = "Ошибка при загрузке корзины"
    ): CartAppError

    data class IncrementProductError(
        override val message: String = "Ошибка при добавлении товара в корзину"
    ) : CartAppError

    data class DecrementProductError(
        override val message: String = "Ошибка при удалении товара с корзины"
    ): CartAppError

    data class DeleteProductError(
        override val message: String = "Ошибка при удалении товара с корзины"
    ): CartAppError

    data class ClearError(
        override val message: String = "Ошибка при очистке корзины"
    ): CartAppError

    data class UnauthorizedError(
        override val message: String = "Вы не авторизованы!"
    ): CartAppError
}