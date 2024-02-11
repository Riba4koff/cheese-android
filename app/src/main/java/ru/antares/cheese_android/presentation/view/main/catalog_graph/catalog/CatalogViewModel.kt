package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class CatalogViewModel(

): ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CatalogViewState> =
        MutableStateFlow(CatalogViewState.Loading())
    val state: StateFlow<CatalogViewState> =
        _mutableStateFlow.asStateFlow()

    init {

    }

    private val _navigationEvents: Channel<CatalogNavigationEvent> = Channel()
    val navigationEvents: Flow<CatalogNavigationEvent> = _navigationEvents.receiveAsFlow()

    fun onEvent() {

    }
}