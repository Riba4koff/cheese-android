package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.domain.usecases.catalog.GetCategoriesUseCase
import java.util.UUID

class CatalogViewModel(
    private val parentID: String,
    private val repository: ICatalogRepository,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CatalogViewState> =
        MutableStateFlow(CatalogViewState.Loading())
    val state: StateFlow<CatalogViewState> =
        _mutableStateFlow.asStateFlow()

    init {
        //load(parentID)
        initialize()
    }

    private val _navigationEvents: Channel<CatalogNavigationEvent> = Channel()
    val navigationEvents: Flow<CatalogNavigationEvent> = _navigationEvents.receiveAsFlow()

    fun onEvent(event: CatalogEvent) {
        /* TODO: handle events */
        when (event) {
            is CatalogEvent.LoadNextPage -> {

            }
        }
    }

    fun onError(error: UIError) {
        when (error as CatalogUIError) {
            is CatalogUIError.Loading -> {
                /* TODO: handle loading error */
            }

            is CatalogUIError.Updating -> {
                /* TODO: handle updating error */
            }
        }
    }

    private suspend fun emitState(state: CatalogViewState) {
        _mutableStateFlow.emit(state)
    }

    private fun initialize() = viewModelScope.launch {
        launch {
            val listOfCategoryPairs = repository.get()

            listOfCategoryPairs.onSuccess { data ->
                getCategoriesUseCase.emit(data)
            }.onFailure { message ->
                emitState(CatalogViewState.Error(error = CatalogUIError.Loading(message)))
            }
        }
        launch {
            getCategoriesUseCase.value.collectLatest { hashMap ->
                if (hashMap.isNotEmpty()) emitState(
                    CatalogViewState.Success(
                        listOfCategoryPairs = hashMap
                    )
                )
            }
        }
    }

    private fun load(parentID: String?) = viewModelScope.launch {
        delay(1000)

        emitState(
            CatalogViewState.Success(
                categories = (1..10).map {
                    CategoryUIModel(
                        id = UUID.randomUUID().toString(), name = "Категория $it"
                    )
                }
            )
        )
    }
}