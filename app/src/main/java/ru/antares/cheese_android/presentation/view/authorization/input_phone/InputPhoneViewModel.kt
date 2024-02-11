package ru.antares.cheese_android.presentation.view.authorization.input_phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository

class InputPhoneViewModel(
    private val repository: IAuthorizationRepository,
    private val tokenService: ITokenService
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<InputPhoneState> =
        MutableStateFlow(InputPhoneState())
    val stateFlow: StateFlow<InputPhoneState> =
        _mutableStateFlow.asStateFlow()

    private val events: Channel<InputPhoneEvent> = Channel()

    private val _navigationEvents: Channel<InputPhoneNavigationEvent> = Channel()
    val navigationEvents: Flow<InputPhoneNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            events.receiveAsFlow().collectLatest { event ->
                when (event) {
                    is InputPhoneEvent.OnPhoneChange -> {
                        _mutableStateFlow.update { state ->
                            state.copy(
                                phone = Regex("[^0-9]").replace(event.value, ""),
                                phoneIsValid = true
                            )
                        }
                        if (event.value.length == 10) makeCall()
                    }

                    InputPhoneEvent.CloseAlertDialog -> _mutableStateFlow.update { state ->
                        state.copy(error = ErrorState())
                    }

                    InputPhoneEvent.SkipAuthorization -> {
                        tokenService.skipAuthorization()
                        _navigationEvents.send(InputPhoneNavigationEvent.NavigateToHomeScreen)
                    }
                }
            }
        }
    }

    fun onEvent(event: InputPhoneEvent) = viewModelScope.launch {
        events.send(event)
    }

    private fun makeCall() = viewModelScope.launch(Dispatchers.IO) {
        setLoading(true)

        val phoneIsValid = isValidPhoneNumber(stateFlow.value.phone)

        _mutableStateFlow.update { state -> state.copy(phoneIsValid = phoneIsValid) }

        if (phoneIsValid) {
            val response = repository.makeCall(phone = "+7"+stateFlow.value.phone)

            when (response) {
                is NetworkResponse.Error -> _mutableStateFlow.update { state ->
                    state.copy(error = ErrorState(isError = true, message = response.message))
                }

                is NetworkResponse.Success -> {
                    val successMakingCall = response.data
                    if (successMakingCall == true) {
                        _navigationEvents.send(InputPhoneNavigationEvent.NavigateToConfirmCode(stateFlow.value.phone))
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

    private fun mockServerResponse(): NetworkResponse<Boolean?> {
        return NetworkResponse.Success(true)
    }
}