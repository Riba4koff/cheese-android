package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import arrow.optics.optics
import ru.antares.cheese_android.domain.models.uiModels.CategoryUIModel
import androidx.compose.runtime.Immutable

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

enum class CatalogUIState {
    LOADING,
    ERROR,
    SUCCESS
}

@optics
@Immutable
data class CatalogState(
    val uiState: CatalogUIState = CatalogUIState.LOADING,
    val categories: List<CategoryUIModel> = emptyList(),
    val listOfCategoryPairs: List<Pair<CategoryUIModel, List<CategoryUIModel>>> = listOf(),
    val sizeResult: Int = 0,
    val page: Int = 0,
    val amountOfPages: Int = 0,
    val amountOfAll: Int = 0,
    val isLoadingNextPage: Boolean = true,
    val error: CatalogUIError? = null
) { companion object }
