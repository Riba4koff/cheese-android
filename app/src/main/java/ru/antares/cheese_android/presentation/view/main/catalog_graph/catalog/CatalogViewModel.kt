package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.repository.ICatalogRepository

class CatalogViewModel(
    private val repository: ICatalogRepository,
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CatalogState> =
        MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _mutableStateFlow.asStateFlow()

    init {
        load(null, null)
    }

    private val _navigationEvents: Channel<CatalogNavigationEvent> = Channel()
    val navigationEvents: Flow<CatalogNavigationEvent> = _navigationEvents.receiveAsFlow()

    fun onEvent(event: CatalogEvent) = viewModelScope.launch {
        /** handle events */
        when (event) {
            is CatalogEvent.LoadNextPage -> {
                load(event.page, event.pageSize)
            }

            /** navigate to products screen */
            is CatalogEvent.NavigateToProducts -> _navigationEvents.send(
                CatalogNavigationEvent.NavigateToProducts(
                    id = event.id,
                    name = event.name
                )
            )

            /** navigate to parent category screen */
            is CatalogEvent.OpenParentCategory -> _navigationEvents.send(
                CatalogNavigationEvent.OpenParentCategory(
                    parentID = event.parentID,
                    name = event.name
                )
            )
        }
    }

    fun onError(error: AppError) {
        when (error) {
            is CatalogAppError.Loading -> {
                /** handle loading error */
                load(null, null)
            }

            is CatalogAppError.Updating -> {
                /** handle updating error */
            }

            is CatalogAppError.UnknownError -> {
                /** handle unknown error */
            }
        }
    }

    private fun load(page: Int?, pageSize: Int?) = viewModelScope.launch(Dispatchers.IO) {
        repository.getListOfCategoryPairs(page, pageSize).collectLatest { resource ->
            resource.onLoading { isLoading ->
                if (isLoading) {
                    _mutableStateFlow.update { state ->
                        state.copy {
                            CatalogState.uiState set CatalogUIState.LOADING
                        }
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CatalogState.error set if (error is CatalogAppError) error
                        else CatalogAppError.UnknownError()
                        CatalogState.uiState set CatalogUIState.ERROR
                    }
                }
            }.onSuccess { pairOfCategories ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CatalogState.listOfCategoryPairs set pairOfCategories
                        CatalogState.uiState set CatalogUIState.SUCCESS
                    }
                }
            }
        }
    }
}