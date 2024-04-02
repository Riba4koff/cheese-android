package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * ProductDetailEvent.kt
 * @author Павел
 * Created by on 22.02.2024 at 22:53
 * Android studio
 */

sealed interface ProductDetailEvent {
    data class AddProductToCart(val product: ProductUIModel) : ProductDetailEvent
    data class RemoveProductFromCart(val product: ProductUIModel) : ProductDetailEvent
    data class LoadNextPageOfRecommendations(
        val categoryID: String, val page: Int, val size: Int
    ) : ProductDetailEvent
}