package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import arrow.optics.optics
import ru.antares.cheese_android.domain.models.CategoryModel
import androidx.compose.runtime.Immutable



@optics
@Immutable
data class CatalogState(
    val uiState: CatalogUIState = CatalogUIState.LOADING,
    val categories: List<CategoryModel> = emptyList(),
    val listOfCategoryPairs: List<Pair<CategoryModel, List<CategoryModel>>> = listOf(),
    val sizeResult: Int = 0,
    val page: Int = 0,
    val amountOfPages: Int = 0,
    val amountOfAll: Int = 0,
    val isLoadingNextPage: Boolean = true,
    val error: CatalogUIError? = null
) { companion object }
