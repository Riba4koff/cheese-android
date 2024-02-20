package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_detail_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.remote.services.main.catalog.models.toCategoryUIModels
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.repository.ICatalogRepository

class CatalogParentCategoryViewModel(
    private val repository: ICatalogRepository,
    parentID: String
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CatalogParentCategoryViewState> =
        MutableStateFlow(CatalogParentCategoryViewState.Loading())
    val state: StateFlow<CatalogParentCategoryViewState> =
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
                /* TODO: - handle loading error*/
            }
        }
    }

    private fun load(parentID: String) = viewModelScope.launch {
        repository.getCategoriesByParentID(parentID, null, null).onFailure { message ->
            emitState(
                CatalogParentCategoryViewState.Error(
                    error = CatalogParentCategoryUIError.Loading(
                        message
                    )
                )
            )
        }.onSuccess { data ->
            emitState(
                CatalogParentCategoryViewState.Success(
                    childCategories = data.result.toCategoryUIModels()
                )
            )
        }
    }

    private suspend fun emitState(state: CatalogParentCategoryViewState) {
        _mutableStateFlow.emit(state)
    }
}