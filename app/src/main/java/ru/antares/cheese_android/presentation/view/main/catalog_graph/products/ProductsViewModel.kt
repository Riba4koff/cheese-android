package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.models.toUIModels
import ru.antares.cheese_android.domain.repository.ICartRepository
import ru.antares.cheese_android.domain.repository.IProductsRepository
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase
import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * ProductsViewModel.kt
 * @author Павел
 * Created by on 21.02.2024 at 0:12
 * Android studio
 */

class ProductsViewModel(
    private val productsRepo: IProductsRepository,
    private val cartRepo: ICartRepository,
    private val categoryID: String,
    private val getCartFlowUseCase: GetCartFlowUseCase
) : ViewModel() {
    companion object {
        const val PAGE_SIZE = 5
    }

    private val _mutableStateFlow: MutableStateFlow<ProductsState> =
        MutableStateFlow(ProductsState())
    val state: StateFlow<ProductsState> =
        _mutableStateFlow.combine(getCartFlowUseCase.entitites) { state, entities ->
            state.copy(
                products = state.products.map { product ->
                    product.copy(
                        countInCart = entities.find { entity ->
                            entity.productID == product.value.id
                        }?.amount ?: 0
                    )
                }
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ProductsState()
        )

    private val _navigationEvents: Channel<ProductsNavigationEvent> = Channel()
    val navigationEvents: Flow<ProductsNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        onEvent(ProductsEvent.LoadNextPage(0, PAGE_SIZE))
    }

    fun onEvent(event: ProductsEvent) = viewModelScope.launch {
        when (event) {
            is ProductsEvent.AddProductToCart -> addProductToCart(event.product)

            is ProductsEvent.RemoveProductFromCart -> removeProductFromCart(event.product)

            is ProductsEvent.LoadNextPage -> loadNextPage(event.page, event.size)
        }
    }

    fun onNavigationEvent(event: ProductsNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(event)
    }

    private fun loadNextPage(page: Int, pageSize: Int) = viewModelScope.launch(Dispatchers.IO) {
        productsRepo.get(
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
                        ProductsState.products set state.products + pagination.result.toUIModels()
                        ProductsState.loading set false
                        ProductsState.currentPage set page
                        ProductsState.endReached set (pagination.sizeResult <= pageSize && pagination.amountOfPages - 1 == page)
                    }
                }
            }
        }
    }

    private fun addProductToCart(product: ProductUIModel) = viewModelScope.launch(Dispatchers.IO) {
        if (!state.value.loadingCart) {
            cartRepo.increment(product.countInCart, product.value.id).collectLatest { resource ->
                resource.onLoading { isLoading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductsState.loadingCart set isLoading
                        }
                    }
                }.onError { error ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductsState.error set error
                        }
                    }
                }
            }
        }
    }

    private fun removeProductFromCart(product: ProductUIModel) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!state.value.loadingCart) {
                cartRepo.decrement(product.countInCart, product.value.id)
                    .collectLatest { resource ->
                        resource.onLoading { isLoading ->
                            _mutableStateFlow.update { state ->
                                state.copy {
                                    ProductsState.loadingCart set isLoading
                                }
                            }
                        }.onError { error ->
                            _mutableStateFlow.update { state ->
                                state.copy {
                                    ProductsState.error set error
                                }
                            }
                        }
                    }
            }
        }

    fun onError(error: AppError) = viewModelScope.launch {
        when (error) {
            is ProductsAppError.LoadingError -> {
                loadNextPage(state.value.currentPage, state.value.pageSize)
            }

            else -> {
                _mutableStateFlow.update { state ->
                    state.copy(error = null)
                }
            }
        }
    }
}