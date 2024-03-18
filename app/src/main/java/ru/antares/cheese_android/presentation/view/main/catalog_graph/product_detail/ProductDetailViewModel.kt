package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

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
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsViewModel

/**
 * ProductDetailViewModel.kt
 * @author Павел
 * Created by on 22.02.2024 at 20:15
 * Android studio
 */

class ProductDetailViewModel(
    private val cartRepo: ICartRepository,
    private val prodRepo: IProductsRepository,
    private val productID: String,
    private val getCartFlowUseCase: GetCartFlowUseCase
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<ProductDetailViewState> =
        MutableStateFlow(ProductDetailViewState())
    val state: StateFlow<ProductDetailViewState> = combine(
        _mutableStateFlow,
        getCartFlowUseCase.value
    ) { state, cart ->
        val countInCart = cart.find { it.productID == state.product?.value?.id }?.amount ?: 0

        state.copy(
            product = state.product?.copy(countInCart = countInCart),
            recommendations = state.recommendations.map { recommendation ->
                val countInCartRec = cart.find { it.productID == recommendation.value.id }?.amount

                if (countInCartRec != null) recommendation.copy(countInCart = countInCartRec)
                else recommendation
            }
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ProductDetailViewState()
    )


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
                addProductToCart(event.product)
            }

            is ProductDetailEvent.RemoveProductFromCart -> {
                removeProductFromCart(event.product)
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

    fun onError(error: AppError) {
        when (error) {
            is ProductDetailAppError.LoadingError -> {
                loadProduct(productID)
            }
            else -> {
                _mutableStateFlow.update { state ->
                    state.copy(appError = null)
                }
            }
        }
    }

    private fun loadProduct(productID: String) = viewModelScope.launch {
        prodRepo.get(productID).collectLatest { resource ->
            resource.onLoading { isLoading ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductDetailViewState.loading set isLoading
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProductDetailViewState.appError set error
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
            prodRepo.get(
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
                            ProductDetailViewState.appError set error
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

    private suspend fun addProductToCart(product: ProductUIModel) {
        if (!state.value.loadingCart) {
            cartRepo.increment(product.countInCart, product.value.id).collectLatest { resource ->
                resource.onLoading { isLoading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductDetailViewState.loadingCart set isLoading
                        }
                    }
                }.onError { error ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductDetailViewState.appError set error
                        }
                    }
                }
            }
        }
    }

    private suspend fun removeProductFromCart(product: ProductUIModel) {
        if (!state.value.loadingCart) {
            cartRepo.decrement(product.countInCart, product.value.id).collectLatest { resource ->
                resource.onLoading { isLoading ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductDetailViewState.loadingCart set isLoading
                        }
                    }
                }.onError { error ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProductDetailViewState.appError set error
                        }
                    }
                }
            }
        }
    }
}