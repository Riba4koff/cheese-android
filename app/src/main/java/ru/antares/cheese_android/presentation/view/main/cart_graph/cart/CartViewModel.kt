package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel

/**
 * @author pavelrybakov
 * Created 27.02.2024 at 13:50
 * Android Studio
 */

class CartViewModel() : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<CartState> = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<CartNavigationEvent> = Channel()
    val navigationEvents: Flow<CartNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _mutableStateFlow.update { state ->
                state.copy(
                    loading = false, products = listOf(
                        CartProductModel(
                            amount = 2,
                            price = 2 * 100.0,
                            priceWithDiscount = null,
                            product = ProductModel(
                                id = "$2",
                                name = "Сыр мастард мастардович",
                                price = 500.0,
                                description = "",
                                unit = 100,
                                category = CategoryModel(
                                    id = "0",
                                    name = "Сыры",
                                    position = 0,
                                    parentID = null
                                ),
                                categoryId = "0",
                                categories = emptyList(),
                                recommend = false,
                                outOfStock = false,
                                unitName = "гр"
                            )
                        )
                    )
                )
            }
        }
    }

    fun onEvent(event: CartEvent) = viewModelScope.launch {
        when (event) {
            is CartEvent.AddProductToCart -> {
                /* TODO: ... */
            }

            is CartEvent.DeleteProductFromCart -> {
                /* TODO: ... */
            }

            is CartEvent.RemoveProductFromCart -> {
                /* TODO: ... */
            }
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
}