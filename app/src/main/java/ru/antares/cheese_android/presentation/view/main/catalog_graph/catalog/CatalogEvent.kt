package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

sealed interface CatalogEvent {
    data class LoadNextPage(
        val page: Int = 0,
        val pageSize: Int = 8
    ): CatalogEvent
}