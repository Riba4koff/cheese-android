package ru.antares.cheese_android.presentation.view.authorization.input_phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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
import ru.antares.cheese_android.data.local.datastore.SecurityTokenService
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository

class InputPhoneViewModel(
    private val repository: IAuthorizationRepository,
    private val tokenService: SecurityTokenService
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<InputPhoneState> =
        MutableStateFlow(InputPhoneState())
    val stateFlow: StateFlow<InputPhoneState> =
        _mutableStateFlow.asStateFlow()

    private val events: Channel<Event> = Channel()

    private val _navigationActions: Channel<NavigationEvent> = Channel()
    val navigationActions: Flow<NavigationEvent> = _navigationActions.receiveAsFlow()

    init {
        viewModelScope.launch {
            events.receiveAsFlow().collectLatest { event ->
                when (event) {
                    is Event.OnPhoneChange -> {
                        _mutableStateFlow.update { state ->
                            state.copy(
                                phone = Regex("[^0-9]").replace(event.value, ""),
                                phoneIsValid = true
                            )
                        }
                        if (event.value.length == 10) makeCall()
                    }

                    Event.CloseAlertDialog -> _mutableStateFlow.update { state ->
                        state.copy(error = ErrorState())
                    }

                    Event.SkipAuthorization -> {
                        tokenService.skipAuthorization()
                        _navigationActions.send(NavigationEvent.NavigateToHomeScreen)
                    }
                }
            }
        }
    }

    fun onEvent(event: Event) = viewModelScope.launch {
        events.send(event)
    }

    private fun makeCall() = viewModelScope.launch(Dispatchers.IO) {
        setLoading(true)

        val phoneIsValid = isValidPhoneNumber(stateFlow.value.phone)

        _mutableStateFlow.update { state -> state.copy(phoneIsValid = phoneIsValid) }

        if (phoneIsValid) {
            delay(1000L)

            val response = mockServerResponse()

            when (response) {
                is NetworkResponse.Error -> _mutableStateFlow.update { state ->
                    state.copy(error = ErrorState(isError = true, message = response.message))
                }

                is NetworkResponse.Success -> {
                    val successMakingCall = response.data.data
                    if (successMakingCall) {
                        _navigationActions.send(NavigationEvent.NavigateToConfirmCode(stateFlow.value.phone))
                    } else _mutableStateFlow.update { state ->
                        state.copy(error = ErrorState(isError = true, message = "Не удалось совершить звонок. Попробуйте позже"))
                    }
                }
            }
        }

        setLoading(false)
    }

    private fun setLoading(value: Boolean) {
        _mutableStateFlow.update { state -> state.copy(isLoading = value) }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberRegex = Regex("^\\+(?:[0-9] ?){6,14}[0-9]$")
        return !phoneNumber.matches(phoneNumberRegex)
    }

    private fun mockServerResponse(): NetworkResponse<MakeCallResponse> {
        //return NetworkResponse.Error("Сервер не отвечает")
        return NetworkResponse.Success(MakeCallResponse(true))
    }
}