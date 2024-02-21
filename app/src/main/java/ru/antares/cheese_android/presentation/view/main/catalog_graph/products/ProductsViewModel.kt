package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

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
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.repository.IProductsRepository

/**
 * ProductsViewModel.kt
 * @author Павел
 * Created by on 21.02.2024 at 0:12
 * Android studio
 */

class ProductsViewModel(
    private val repository: IProductsRepository, private val categoryID: String
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 4
    }

    private val _mutableStateFlow: MutableStateFlow<ProductsState> =
        MutableStateFlow(ProductsState())
    val state: StateFlow<ProductsState> = _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<ProductsNavigationEvent> = Channel()
    val navigationEvents: Flow<ProductsNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        onEvent(ProductsEvent.LoadNextPage(0, PAGE_SIZE))
    }

    fun onEvent(event: ProductsEvent) = viewModelScope.launch {
        when (event) {
            is ProductsEvent.AddProductToCart -> {

            }

            is ProductsEvent.OnProductClick -> {

            }

            is ProductsEvent.RemoveProductFromCart -> {

            }

            is ProductsEvent.LoadNextPage -> loadNextPage(event.page, event.size)
        }
    }

    fun onNavigationEvent(event: ProductsNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(event)
    }

    private fun loadNextPage(page: Int, pageSize: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.get(
            categoryID = categoryID, page = page, size = PAGE_SIZE
        ).collectLatest { resource ->
            resource.onLoading { isLoading ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductsState.loadingNextPage set (isLoading && page > 0)
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductsState.error set error
                    }
                }
            }.onSuccess { pagination ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductsState.products set state.products + pagination.result
                        ProductsState.loading set false
                        ProductsState.currentPage set page
                        ProductsState.endReached set (pagination.sizeResult <= pageSize && pagination.amountOfPages - 1 == page)
                    }
                }
            }
        }
    }

    fun onError(error: UIError) = viewModelScope.launch {
        when (error as ProductsUIError) {
            is ProductsUIError.LoadingError -> {
                /*TODO: handle error*/
            }
        }
    }
}