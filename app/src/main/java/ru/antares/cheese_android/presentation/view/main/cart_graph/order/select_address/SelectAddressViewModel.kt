package ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
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
import ru.antares.cheese_android.data.local.room.addresses.AddressesLocalStorage
import ru.antares.cheese_android.domain.repository.IAddressesRepository

/**
 * @author pavelrybakov
 * Created 20.05.2024 at 14:48
 * Android Studio
 */

class SelectAddressViewModel(
    private val repository: IAddressesRepository,
    addressesLocalStorage: AddressesLocalStorage
) : ViewModel() {
    private val _mutableState: MutableStateFlow<SelectAddressScreenState> =
        MutableStateFlow(SelectAddressScreenState())
    val state: StateFlow<SelectAddressScreenState> =
        _mutableState.combine(addressesLocalStorage.get()) { state, addresses ->
            state.copy {
                SelectAddressScreenState.addresses set addresses
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SelectAddressScreenState()
        )

    private val _navigationEvents: Channel<SelectAddressNavigationEvent> = Channel()
    val navigationEvents: Flow<SelectAddressNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        onEvent(SelectAddressEvent.LoadNextPage(0, 16))
    }

    fun onEvent(event: SelectAddressEvent) {
        when (event) {
            is SelectAddressEvent.SetAddress -> selectAddress(event)
            is SelectAddressEvent.LoadNextPage -> loadNextPage(event)
        }
    }

    fun onNavigationEvent(event: SelectAddressNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    private fun selectAddress(event: SelectAddressEvent.SetAddress) {
        _mutableState.update { state ->
            state.copy {
                SelectAddressScreenState.address set event.address
            }
        }
    }

    private fun loadNextPage(
        event: SelectAddressEvent.LoadNextPage
    ) {
        viewModelScope.launch {
            repository.get(
                event.page,
                event.size
            ).collectLatest { receivingAddresses ->
                receivingAddresses.onLoading { loading ->
                    _mutableState.update { state ->
                        state.copy {
                            SelectAddressScreenState.loadingNextPage set loading
                        }
                    }
                }.onSuccess { pagination ->
                    _mutableState.update { state ->
                        state.copy {
                            SelectAddressScreenState.loading set false
                            SelectAddressScreenState.currentPage set pagination.page
                            SelectAddressScreenState.endReached set (pagination.sizeResult <= event.size && pagination.amountOfPages - 1 == event.page)
                        }
                    }
                }
            }
        }
    }
}