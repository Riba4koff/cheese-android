package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.local.room.addresses.AddressesLocalStorage
import ru.antares.cheese_android.domain.repository.IAddressesRepository

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 16:04
 * Android Studio
 */

class AddressesViewModel(
    private val repository: IAddressesRepository,
    private val addressesLocalStorage: AddressesLocalStorage
) : ViewModel() {
    private val _mutableState: MutableStateFlow<AddressesScreenState> =
        MutableStateFlow(AddressesScreenState())
    val state: StateFlow<AddressesScreenState> = combine(
        _mutableState,
        addressesLocalStorage.get()
    ) { state, addresses ->
        state.copy(
            addresses = addresses
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        AddressesScreenState()
    )

    private val _navigationEvents = Channel<AddressesNavigationEvent>()
    val navigationEvents = _navigationEvents.receiveAsFlow()

    init {
        onEvent(AddressesEvent.LoadNextPage(0, 16))
    }

    fun onEvent(event: AddressesEvent) {
        when (event) {
            is AddressesEvent.RemoveAddress -> removeAddress(event.id)
            is AddressesEvent.LoadNextPage -> loadNextPage(event.page, event.size)
        }
    }

    fun onNavigationEvent(event: AddressesNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    private fun loadNextPage(page: Int, size: Int) {
        viewModelScope.launch {
            repository.get(page, size).collectLatest { receivingAddresses ->
                receivingAddresses.onLoading { loading ->
                    _mutableState.update { state ->
                        state.copy {
                            AddressesScreenState.loadingNextPage set loading
                        }
                    }
                }.onSuccess { pagination ->
                    _mutableState.update { state ->
                        state.copy {
                            AddressesScreenState.loading set false
                            AddressesScreenState.currentPage set pagination.page
                            AddressesScreenState.endReached set (pagination.sizeResult <= size && pagination.amountOfPages - 1 == page)
                        }
                    }
                }
            }
        }
    }

    private fun removeAddress(id: String) {
        viewModelScope.launch {
            repository.remove(id).collectLatest { resource ->
                resource.onLoading { isLoading ->
                    _mutableState.update { state ->
                        state.copy {
                            AddressesScreenState.loading set isLoading
                        }
                    }
                }
            }
        }
    }
}