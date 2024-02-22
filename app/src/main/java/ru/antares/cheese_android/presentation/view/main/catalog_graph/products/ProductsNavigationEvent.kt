package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import ru.antares.cheese_android.domain.errors.ProductModel

/**
 * ProductsNavigationEvent.kt
 * @author Павел
 * Created by on 21.02.2024 at 0:17
 * Android studio
 */

sealed interface ProductsNavigationEvent {
    data class NavigateToProductDetailInfo(val product: ProductModel): ProductsNavigationEvent
    data object GoBack: ProductsNavigationEvent
}