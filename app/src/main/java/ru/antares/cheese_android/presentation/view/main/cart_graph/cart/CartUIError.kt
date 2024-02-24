package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import ru.antares.cheese_android.domain.errors.UIError

/**
 * CartUIError.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:38
 * Android studio
 */

sealed interface CartUIError : UIError {
    data class LoadCartError(
        override val message: String = "Ошибка при загрузке корзины"
    ): CartUIError

    data class IncrementProductError(
        override val message: String = "Ошибка при добавлении товара в корзину"
    ) : CartUIError

    data class DecrementProductError(
        override val message: String = "Ошибка при удалении товара с корзины"
    ): CartUIError

    data class DeleteProductError(
        override val message: String = "Ошибка при удалении товара с корзины"
    ): CartUIError

    data class ClearError(
        override val message: String = "Ошибка при очистке корзины"
    ): CartUIError
}