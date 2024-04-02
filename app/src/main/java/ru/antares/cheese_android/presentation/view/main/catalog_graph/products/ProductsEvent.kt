package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * ProductsEvent.kt
 * @author Павел
 * Created by on 21.02.2024 at 0:15
 * Android studio
 */

sealed interface ProductsEvent {
    data class AddProductToCart(val product: ProductUIModel) : ProductsEvent
    data class RemoveProductFromCart(val product: ProductUIModel) : ProductsEvent
    data class LoadNextPage(val page: Int, val size: Int): ProductsEvent
}