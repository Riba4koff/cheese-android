package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.remote.api.main.addresses.request.CreateAddressRequest
import ru.antares.cheese_android.domain.repository.IAddressesRepository

/**
 * @author pavelrybakov
 * Created 17.05.2024 at 18:02
 * Android Studio
 */

class CreateAddressViewModel(
    private val repository: IAddressesRepository,
) : ViewModel() {
    private val _mutableState: MutableStateFlow<CreateAddressScreenState> =
        MutableStateFlow(CreateAddressScreenState())
    val state: StateFlow<CreateAddressScreenState> = _mutableState.asStateFlow()

    private val _navigationEventChannel = Channel<CreateAddressNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEventChannel.receiveAsFlow()

    fun onEvent(event: CreateAddressEvent) {
        when (event) {
            is CreateAddressEvent.OnApartmentChange -> onApartmentChange(event.value)
            is CreateAddressEvent.OnBuildingChange -> onBuildingChange(event.value)
            is CreateAddressEvent.OnCityChange -> onCityChange(event.value)
            is CreateAddressEvent.OnCommentChange -> onCommentChange(event.value)
            is CreateAddressEvent.OnEntranceChange -> onEntranceChange(event.value)
            is CreateAddressEvent.OnFloorChange -> onFloorChange(event.value)
            is CreateAddressEvent.OnStreetChange -> onStreetChange(event.value)
            is CreateAddressEvent.OnHouseChange -> onHouseChange(event.value)
            is CreateAddressEvent.OnSaveClick -> save(
                city = event.city,
                street = event.street,
                house = event.house,
                building = event.building,
                entrance = event.entrance,
                floor = event.floor,
                apartment = event.apartment,
                comment = event.comment
            )
        }
    }

    fun onNavigationEvent(event: CreateAddressNavigationEvent) {
        viewModelScope.launch {
            _navigationEventChannel.send(event)
        }
    }

    private fun onCityChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.city set value
            }
        }
    }

    private fun onStreetChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.street set value
            }
        }
    }

    private fun onHouseChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.house set value
            }
        }
    }

    private fun onBuildingChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.building set value
            }
        }
    }

    private fun onEntranceChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.entrance set value
            }
        }
    }

    private fun onFloorChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.floor set value
            }
        }
    }

    private fun onApartmentChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.apartment set value
            }
        }
    }

    private fun onCommentChange(value: String) {
        _mutableState.update { state ->
            state.copy {
                CreateAddressScreenState.comment set value
            }
        }
    }

    private fun save(
        city: String,
        street: String,
        house: String,
        building: String,
        entrance: String,
        floor: String,
        apartment: String,
        comment: String
    ) {
        viewModelScope.launch {
            repository.create(
                request = CreateAddressRequest(
                    city = city,
                    house = house,
                    street = street,
                    building = building.ifEmpty { null },
                    entrance = entrance.ifEmpty { null },
                    floor = floor.ifEmpty { null },
                    apartment = apartment.ifEmpty { null },
                    title = comment
                )
            ).collectLatest { result ->
                result.onLoading { loading ->
                    _mutableState.update { state ->
                        state.copy {
                            CreateAddressScreenState.loading set loading
                        }
                    }
                }.onSuccess { _ ->
                    onNavigationEvent(CreateAddressNavigationEvent.NavigateBack)
                }
            }
        }
    }
}