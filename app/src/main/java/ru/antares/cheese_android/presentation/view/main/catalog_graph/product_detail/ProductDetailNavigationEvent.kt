package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

/**
 * ProductDetailNavigationEvent.kt
 * @author Павел
 * Created by on 22.02.2024 at 23:06
 * Android studio
 */

sealed interface ProductDetailNavigationEvent {
    data object GoBack : ProductDetailNavigationEvent
    data class NavigateToFeedBack(val productID: String) : ProductDetailNavigationEvent
    data class NavigateToProduct(val productID: String) : ProductDetailNavigationEvent
}