package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.repository.main.CartRepository
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:50
 * Android Studio
 */

class CartViewModel(
    private val getCartFlowUseCase: GetCartFlowUseCase,
    private val repository: CartRepository
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CartState> = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<CartNavigationEvent> = Channel()
    val navigationEvents: Flow<CartNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch {
                getCartFlowUseCase.productsValue.collectLatest { products ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            CartState.products set products
                            CartState.totalCost set products.sumOf { it.price * it.amount }
                        }
                    }
                }
            }
            launch {
                repository.get().collectLatest { resourceState ->
                    resourceState.onError { uiError ->
                        _mutableStateFlow.update { state ->
                            state.copy {
                                CartState.error set uiError
                            }
                        }
                    }.onSuccess { response ->
                        _mutableStateFlow.update { state ->
                            state.copy {
                                CartState.totalCost set response.totalCost
                                CartState.totalCostWithDiscount set response.totalCostWithDiscount
                                CartState.currentPage set response.page
                                CartState.loading set false
                            }
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: CartEvent) = viewModelScope.launch {
        when (event) {
            is CartEvent.AddProductToCart -> addProductToCart(
                amount = event.amount,
                productID = event.productID
            )

            is CartEvent.RemoveProductFromCart -> removeProductFromCart(
                amount = event.amount,
                productID = event.productID
            )

            is CartEvent.DeleteProductFromCart -> deleteProductFromCart(
                productID = event.productID
            )
        }
    }

    fun onNavigationEvent(event: CartNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(event)
    }

    fun onError(error: UIError) {
        when (error) {
            is CartUIError.ClearError -> {
                /* TODO() ... */
            }

            is CartUIError.DecrementProductError -> {
                /* TODO() ... */
            }

            is CartUIError.DeleteProductError -> {
                /* TODO() ... */
            }

            is CartUIError.IncrementProductError -> {
                /* TODO() ... */
            }

            is CartUIError.LoadCartError -> {
                /* TODO() ... */
            }
        }
    }

    private suspend fun addProductToCart(amount: Int, productID: String) {
        repository.increment(amount, productID).collectLatest { resource ->
            resource.onLoading { isLoading ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CartState.cartLoading set isLoading
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CartState.error set error
                    }
                }
            }
        }
    }

    private suspend fun removeProductFromCart(amount: Int, productID: String) {
        repository.decrement(amount, productID).collectLatest { resource ->
            resource.onLoading { isLoading ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CartState.cartLoading set isLoading
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CartState.error set error
                    }
                }
            }
        }
    }

    private suspend fun deleteProductFromCart(productID: String) {
        repository.delete(productID).collectLatest { resource ->
            resource.onLoading { isLoading ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CartState.cartLoading set isLoading
                    }
                }
            }.onError { error ->
                _mutableStateFlow.update { state ->
                    state.copy {
                        CartState.error set error
                    }
                }
            }
        }
    }
}