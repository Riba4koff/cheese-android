package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import ru.antares.cheese_android.domain.models.ProductModel

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:37
 * Android Studio
 */

sealed interface CartEvent {
    data class AddProductToCart(val product: ProductModel): CartEvent
    data class RemoveProductFromCart(val product: ProductModel): CartEvent
    data class DeleteProductFromCart(val product: ProductModel): CartEvent
}