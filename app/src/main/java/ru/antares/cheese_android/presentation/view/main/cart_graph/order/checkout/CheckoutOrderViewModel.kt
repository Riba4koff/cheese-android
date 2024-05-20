package ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.room.addresses.AddressesLocalStorage
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase

/**
 * @author pavelrybakov
 * Created 18.03.2024 at 16:46
 * Android Studio
 */

class CheckoutOrderViewModel(
    getCartFlowUseCase: GetCartFlowUseCase,
    private val addressesLocalStorage: AddressesLocalStorage
) : ViewModel() {
    private val _mutableState: MutableStateFlow<CheckoutOrderState> =
        MutableStateFlow(CheckoutOrderState())
    val state: StateFlow<CheckoutOrderState> = combine(
        _mutableState,
        getCartFlowUseCase.products
    ) { state, products ->
        state.copy(
            products = products
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        CheckoutOrderState()
    )

    private val _navigationEvents: Channel<CheckoutOrderNavigationEvent> = Channel()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private fun setState(state: CheckoutOrderState) {
        _mutableState.update { state }
    }

    fun onEvent(event: CheckoutOrderEvent) {
        when (event) {
            is CheckoutOrderEvent.OnCommentChange -> onCommentChange(event.comment)
            is CheckoutOrderEvent.OnReceiverChange -> onReceiverChange(event.receiver)
            is CheckoutOrderEvent.OnPaymentMethodChange -> onPaymentMethodChange(event.paymentMethod)
            is CheckoutOrderEvent.OnAddressChange -> onAddressChange(event.addressID)
        }
    }

    fun onNavigationEvent(navigationEvent: CheckoutOrderNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(navigationEvent)
        }
    }

    fun onError(error: AppError) {
        if (error is CheckoutOrderAppError) {
            when (error as CheckoutOrderAppError) {
                else -> {
                    /* TODO: ... */
                }
            }
        }
    }

    private fun onCommentChange(comment: String) {
        setState(state.value.copy(comment = comment))
    }

    private fun onReceiverChange(receiver: String) {
        setState(state.value.copy(receiver = receiver))
    }

    private fun onPaymentMethodChange(paymentMethod: PaymentType) {
        setState(
            state.value.copy(
                paymentMethod = if (state.value.paymentMethod == paymentMethod) null
                else paymentMethod
            )
        )
    }

    private fun onAddressChange(addressID: String) {
        viewModelScope.launch {
            val address = addressesLocalStorage.get(addressID)

            _mutableState.update { state ->
                state.copy(
                    address = address
                )
            }
        }
    }
}