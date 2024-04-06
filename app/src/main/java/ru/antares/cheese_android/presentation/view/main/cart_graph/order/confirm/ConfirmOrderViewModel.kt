package ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.user.UserDataStore
import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.domain.paymentType.getPaymentType
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase

/**
 * @author pavelrybakov
 * Created 29.03.2024 at 15:57
 * Android Studio
 */

class ConfirmOrderViewModel(
    cartFlowUseCase: GetCartFlowUseCase,
    addressID: String,
    receiver: String,
    paymentType: String,
    comment: String,
    totalCost: Float,
    userDataStore: UserDataStore
) : ViewModel() {
    private val _mutableState: MutableStateFlow<ConfirmOrderState> =
        MutableStateFlow(ConfirmOrderState())
    val state: StateFlow<ConfirmOrderState> =
        _mutableState.combine(cartFlowUseCase.products) { state, cart ->
            state.copy(products = cart)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            initialValue = ConfirmOrderState()
        )

    private val _navigationEvents: Channel<ConfirmOrderNavigationEvent> = Channel()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private fun setState(state: ConfirmOrderState) {
        _mutableState.update { state }
    }

    init {
        viewModelScope.launch {
            val orderReceiver = if (receiver == "Не указан") {
                userDataStore
                    .user
                    .first()
                    .credentials()
            } else receiver
            setState(
                ConfirmOrderState(
                    address = null, /*TODO: make DB and get selected address*/
                    receiver = orderReceiver,
                    paymentMethod = getPaymentType(PaymentType.Type.valueOf(paymentType)),
                    comment = comment,
                    totalCost = totalCost.toDouble()
                )
            )
        }
    }

    fun onNavigationEvent(event: ConfirmOrderNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    fun onEvent(event: ConfirmOrderEvent) {
        viewModelScope.launch {
            when (event) {
                ConfirmOrderEvent.Pay -> pay()
            }
        }
    }

    private suspend fun pay() {
        loading {
            /* TODO make payment call */
            delay(1000)
        }
    }

    private suspend fun loading(block: suspend () -> Unit) {
        setState(state.value.copy(loading = true))
        block()
        setState(state.value.copy(loading = false))
    }
}