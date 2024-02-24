package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

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
import ru.antares.cheese_android.domain.models.toUIModels
import ru.antares.cheese_android.domain.repository.IProductsRepository
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsViewModel

/**
 * ProductDetailViewModel.kt
 * @author Павел
 * Created by on 22.02.2024 at 20:15
 * Android studio
 */

class ProductDetailViewModel(
    private val repository: IProductsRepository,
    private val productID: String
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<ProductDetailViewState> =
        MutableStateFlow(ProductDetailViewState())
    val state: StateFlow<ProductDetailViewState> = _mutableStateFlow.asStateFlow()


    private val _navigationEvents: Channel<ProductDetailNavigationEvent> = Channel()
    val navigationEvents: Flow<ProductDetailNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        loadProduct(productID)
    }

    fun onNavigationEvent(event: ProductDetailNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(event)
    }

    fun onEvent(event: ProductDetailEvent) = viewModelScope.launch {
        when (event) {
            is ProductDetailEvent.AddProductToCart -> {
                /*TODO: - add product to cart */
            }

            is ProductDetailEvent.RemoveProductFromCart -> {
                /*TODO: - remove product from cart */
            }

            is ProductDetailEvent.LoadNextPageOfRecommendations -> {
                loadNextPageOfRecommendations(
                    categoryID = event.categoryID,
                    page = event.page,
                    pageSize = event.size
                )
            }
        }
    }

    fun onError(error: UIError) {
        when (error as ProductDetailUIError) {
            is ProductDetailUIError.LoadingError -> {
                _mutableStateFlow.update { state -> state.copy(uiError = null) }
                loadProduct(productID)
            }
        }
    }

    private fun loadProduct(productID: String) = viewModelScope.launch {
        repository.get(productID).collectLatest { resource ->
            resource.onLoading { isLoading ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductDetailViewState.loading set isLoading
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductDetailViewState.uiError set error
                    }
                }
            }.onSuccess { product ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductDetailViewState.product set ProductUIModel(product, 0)
                    }
                }
            }
        }
    }

    private fun loadNextPageOfRecommendations(categoryID: String, page: Int, pageSize: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.get(
                categoryID = categoryID, page = page, size = ProductsViewModel.PAGE_SIZE
            ).collectLatest { resource ->
                resource.onLoading { isLoading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductDetailViewState.loadingNextPageRecommendations set isLoading
                        }
                    }
                }.onError { error ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductDetailViewState.uiError set error
                        }
                    }
                }.onSuccess { pagination ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductDetailViewState.recommendations set state.recommendations + pagination.result.toUIModels()
                            ProductDetailViewState.loading set false
                            ProductDetailViewState.currentPage set page
                            ProductDetailViewState.endReached set (pagination.sizeResult <= pageSize && pagination.amountOfPages - 1 == page)
                        }
                    }
                }
            }
        }
}