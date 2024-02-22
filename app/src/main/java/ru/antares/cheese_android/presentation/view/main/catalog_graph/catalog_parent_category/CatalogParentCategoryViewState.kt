package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category

import arrow.optics.optics
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.presentation.models.CategoryUIModel
import javax.annotation.concurrent.Immutable

sealed interface CatalogParentCategoryViewState {
    val key: CatalogParentCategoryViewStateKey

    data class Loading(
        override val key: CatalogParentCategoryViewStateKey = CatalogParentCategoryViewStateKey.Loading
    ) : CatalogParentCategoryViewState

    data class Error(
        val error: CatalogParentCategoryUIError,
        override val key: CatalogParentCategoryViewStateKey = CatalogParentCategoryViewStateKey.Error
    ) : CatalogParentCategoryViewState

    data class Success(
        override val key: CatalogParentCategoryViewStateKey = CatalogParentCategoryViewStateKey.Success,
        val childCategories: List<CategoryUIModel> = emptyList(),
        val loadingNextPage: Boolean = false,
        val currentPage: Int = 0
    ) : CatalogParentCategoryViewState
}

enum class CatalogParentCategoryUIState {
    LOADING,
    ERROR,
    SUCCESS
}

@optics
@Immutable
data class CatalogParentCategoryState(
    val uiState: CatalogParentCategoryUIState = CatalogParentCategoryUIState.LOADING,
    val childCategories: List<CategoryUIModel> = emptyList(),
    val loadingNextPage: Boolean = false,
    val currentPage: Int = 0,
    val error: UIError? = null,
) { companion object }
