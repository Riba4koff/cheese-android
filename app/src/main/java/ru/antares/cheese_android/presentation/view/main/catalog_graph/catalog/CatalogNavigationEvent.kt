package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

sealed interface CatalogNavigationEvent {
    data class OpenParentCategory(
        val parentID: String,
        val name: String
    ): CatalogNavigationEvent

    data class NavigateToProducts(
        val id: String,
        val name: String
    ): CatalogNavigationEvent
}