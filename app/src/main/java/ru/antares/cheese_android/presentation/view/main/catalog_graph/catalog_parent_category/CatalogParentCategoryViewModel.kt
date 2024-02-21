package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.remote.dto.toCategoryUIModels
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogState
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogUIState
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.uiState

class CatalogParentCategoryViewModel(
    private val repository: ICatalogRepository,
    private val parentID: String
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CatalogParentCategoryState> =
        MutableStateFlow(CatalogParentCategoryState())
    val state: StateFlow<CatalogParentCategoryState> =
        _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<CatalogParentCategoryNavigationEvent> = Channel()
    val navigationEvent: Flow<CatalogParentCategoryNavigationEvent> =
        _navigationEvents.receiveAsFlow()

    init {
        load(parentID)
    }

    fun onEvent(event: CatalogParentCategoryEvent) = viewModelScope.launch {
        when (event) {
            is CatalogParentCategoryEvent.OnCategoryClick -> {
                /* TODO: - navigate to products screen */
                _navigationEvents.send(
                    CatalogParentCategoryNavigationEvent.NavigateToProducts(
                        id = event.id,
                        categoryName = event.name
                    )
                )
            }

            is CatalogParentCategoryEvent.LoadNextPage -> {
                /* TODO() - pagination logic */
            }
        }
    }

    fun onError(error: UIError) {
        when (error as CatalogParentCategoryUIError) {
            is CatalogParentCategoryUIError.Loading -> {
                load(parentID = parentID)
            }
        }
    }

    private fun load(parentID: String) = viewModelScope.launch {
        repository.getCategoriesByParentID(parentID, null, null).collectLatest { resource ->
            resource.onLoading { isLoading ->
                if (isLoading) {
                    _mutableStateFlow.update { state ->
                        state.copy {
                            CatalogParentCategoryState.uiState set CatalogParentCategoryUIState.LOADING
                        }
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CatalogParentCategoryState.error set error
                        CatalogParentCategoryState.uiState set CatalogParentCategoryUIState.ERROR
                    }
                }
            }.onSuccess { pagination ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CatalogParentCategoryState.childCategories set pagination.result
                        CatalogParentCategoryState.uiState set CatalogParentCategoryUIState.SUCCESS
                        CatalogParentCategoryState.currentPage set pagination.page
                    }
                }
            }
        }
    }
}