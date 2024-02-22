package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import arrow.optics.optics
import ru.antares.cheese_android.presentation.models.CategoryUIModel
import androidx.compose.runtime.Immutable



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
