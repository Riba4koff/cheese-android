package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel

sealed interface CatalogViewState {
    val key: CatalogViewStateKey

    data class Loading(
        override val key: CatalogViewStateKey = CatalogViewStateKey.Loading
    ) : CatalogViewState

    data class Error(
        override val key: CatalogViewStateKey = CatalogViewStateKey.Error,
        val error: UIError
    ) : CatalogViewState

    data class Success(
        override val key: CatalogViewStateKey = CatalogViewStateKey.Success,
        val categories: List<CategoryUIModel> = emptyList(),
        val listOfCategoryPairs: List<Pair<CategoryUIModel, List<CategoryUIModel>>> = listOf(),
        val sizeResult: Int = 0,
        val page: Int = 0,
        val amountOfPages: Int = 0,
        val amountOfAll: Int = 0,
        val isLoadingNextPage: Boolean = true
        /* state here ... */
    ) : CatalogViewState
}