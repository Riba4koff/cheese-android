package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

sealed interface CatalogUIError {
    data class Loading(val message: String): CatalogUIError
    data class Updating(val message: String): CatalogUIError
}