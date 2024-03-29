package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_detail_category

import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel

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