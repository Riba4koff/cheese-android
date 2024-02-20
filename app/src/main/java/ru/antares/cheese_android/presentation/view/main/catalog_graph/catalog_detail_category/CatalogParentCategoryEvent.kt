package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_detail_category

sealed interface CatalogParentCategoryEvent {
    data class OnCategoryClick(
        val id: String,
        val name: String
    ) : CatalogParentCategoryEvent

    data class LoadNextPage(
        val parentID: String,
        val page: Int,
        val pageSize: Int
    ) : CatalogParentCategoryEvent
}