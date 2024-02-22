package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import ru.antares.cheese_android.domain.errors.ProductModel

/**
 * ProductsEvent.kt
 * @author Павел
 * Created by on 21.02.2024 at 0:15
 * Android studio
 */

sealed interface ProductsEvent {
    data class OnProductClick(val product: ProductModel) : ProductsEvent
    data class AddProductToCart(val product: ProductModel) : ProductsEvent
    data class RemoveProductFromCart(val product: ProductModel) : ProductsEvent
    data class LoadNextPage(val page: Int, val size: Int): ProductsEvent
}