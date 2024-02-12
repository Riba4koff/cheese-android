package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.repository.ICatalogRepository

class CatalogViewModel(
    private val repository: ICatalogRepository,
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CatalogViewState> =
        MutableStateFlow(CatalogViewState.Loading())
    val state: StateFlow<CatalogViewState> =
        _mutableStateFlow.asStateFlow()

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

    fun onError(error: UIError) {
        when (error as CatalogUIError) {
            is CatalogUIError.Loading -> {
                /** handle loading error */
                load(null, null)
            }

            is CatalogUIError.Updating -> {
                /** handle updating error */
            }
        }
    }

    private suspend fun emitState(newState: CatalogViewState) {
        _mutableStateFlow.emit(newState)
    }

    private fun load(page: Int?, pageSize: Int?) = viewModelScope.launch {
        repository.getListOfCategoryPairs(page, pageSize).collect(_mutableStateFlow)
    }
}