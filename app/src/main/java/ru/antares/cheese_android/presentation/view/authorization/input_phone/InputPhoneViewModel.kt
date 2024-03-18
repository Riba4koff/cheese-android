package ru.antares.cheese_android.presentation.view.authorization.input_phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository

class InputPhoneViewModel(
    private val repository: IAuthorizationRepository,
    private val tokenService: IAuthorizationDataStore
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
                        if (event.value.length == 10) makeCallV2()
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

    private fun makeCallV2() = viewModelScope.launch {
        val correctPhone = "+7" + stateFlow.value.phone

        val phoneIsValid = isValidPhoneNumber(stateFlow.value.phone)

        if (phoneIsValid) {
            repository.makeCallV2(correctPhone).collectLatest { resourceState ->
                resourceState.onLoading { isLoading ->
                    setLoading(isLoading)
                }.onError { error ->
                    if (error is InputPhoneAppError) {
                        _mutableStateFlow.update { state ->
                            state.copy(error = error)
                        }
                    } else {
                        _mutableStateFlow.update { state ->
                            state.copy(error = InputPhoneAppError.UnknownError())
                        }
                    }
                }.onSuccess { callIsSuccess ->
                    when (callIsSuccess) {
                        true -> _navigationEvents.send(
                            InputPhoneNavigationEvent.NavigateToConfirmCode(stateFlow.value.phone)
                        )

                        false -> _mutableStateFlow.update { state ->
                            state.copy(error = InputPhoneAppError.MakeCallError())
                        }

                        else -> _mutableStateFlow.update { state ->
                            state.copy(error = InputPhoneAppError.UnknownError())
                        }
                    }
                }
            }
        }
    }

    private fun setLoading(value: Boolean) {
        _mutableStateFlow.update { state -> state.copy(isLoading = value) }
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberRegex = Regex("^\\+(?:[0-9] ?){6,14}[0-9]$")
        return !phoneNumber.matches(phoneNumberRegex)
    }

    fun onError(appError: AppError) {
        when (appError as InputPhoneAppError) {
            is InputPhoneAppError.ServerError -> {
                _mutableStateFlow.update { state ->
                    state.copy(error = null)
                }
            }

            is InputPhoneAppError.MakeCallError -> {
                _mutableStateFlow.update { state ->
                    state.copy(error = null)
                }
            }

            is InputPhoneAppError.UnknownError -> {
                _mutableStateFlow.update { state ->
                    state.copy(error = null)
                }
            }
        }
    }
}