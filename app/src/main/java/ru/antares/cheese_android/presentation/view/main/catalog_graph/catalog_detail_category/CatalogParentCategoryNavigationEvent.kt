package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_detail_category

sealed interface CatalogParentCategoryNavigationEvent {
    data class NavigateToProducts(val id: String, val categoryName: String): CatalogParentCategoryNavigationEvent
}