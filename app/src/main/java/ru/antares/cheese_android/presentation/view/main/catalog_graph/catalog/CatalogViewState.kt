package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

sealed interface CatalogViewState {
    val key: CatalogViewStateKey

    data class Loading(
        override val key: CatalogViewStateKey = CatalogViewStateKey.Loading
    ) : CatalogViewState

    data class Error(
        override val key: CatalogViewStateKey = CatalogViewStateKey.Error,
        val error: CatalogUIError
    ) : CatalogViewState

    data class Success(
        override val key: CatalogViewStateKey = CatalogViewStateKey.Success
        /* state here ... */
    ) : CatalogViewState
}