package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import ru.antares.cheese_android.domain.models.ProductModel

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:37
 * Android Studio
 */

sealed interface CartEvent {
    data class AddProductToCart(val productID: String, val amount: Int): CartEvent
    data class RemoveProductFromCart(val productID: String, val amount: Int): CartEvent
    data class DeleteProductFromCart(val productID: String): CartEvent
}