package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.repository.main.CartRepository
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:50
 * Android Studio
 */

class CartViewModel(
    getCartFlowUseCase: GetCartFlowUseCase,
    private val repository: CartRepository
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CartState> = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<CartNavigationEvent> = Channel()
    val navigationEvents: Flow<CartNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        getCartFlowUseCase.products.map { products ->
            _mutableStateFlow.update { state ->
                state.copy {
                    CartState.products set products
                    CartState.totalCost set products.sumOf { it.price * it.amount }
                }
            }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            repository.getV2().collectLatest { resourceState ->
                resourceState.onError { _ ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            CartState.loading set false
                        }
                    }
                }.onSuccess { response ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            CartState.totalCost set response.totalCost
                            CartState.totalCostWithDiscount set response.totalCostWithDiscount
                            CartState.currentPage set response.page
                            CartState.loading set false
                            CartState.authorized set true
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

    fun onError(error: AppError) {
        when (error) {
            is CartAppError.ClearError -> {
                /* TODO() ... */
            }

            is CartAppError.DecrementProductError -> {
                /* TODO() ... */
            }

            is CartAppError.DeleteProductError -> {
                /* TODO() ... */
            }

            is CartAppError.IncrementProductError -> {
                /* TODO() ... */
            }

            is CartAppError.LoadCartError -> {
                /* TODO() ... */
            }

            is CartAppError.UnauthorizedError -> {
                _mutableStateFlow.update { state ->
                    state.copy(error = null)
                }
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