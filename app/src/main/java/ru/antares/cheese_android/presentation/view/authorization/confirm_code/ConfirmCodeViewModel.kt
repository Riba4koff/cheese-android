package ru.antares.cheese_android.presentation.view.authorization.confirm_code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.remote.services.auth.dto.DeviceDTO
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository

class ConfirmCodeViewModel(
    private val phone: String,
    private val repository: IAuthorizationRepository,
    private val tokenService: IAuthorizationDataStore
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<ConfirmCodeState> =
        MutableStateFlow(ConfirmCodeState())
    val stateFlow: StateFlow<ConfirmCodeState> =
        _mutableStateFlow.asStateFlow()

    private val events: Channel<ConfirmCodeEvent> = Channel()

    private val _navigationEvents: Channel<ConfirmCodeNavigationEvent> = Channel()
    val navigationEvents: Flow<ConfirmCodeNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch { startTimer() }
            launch {
                events.receiveAsFlow().collectLatest { event ->
                    when (event) {
                        is ConfirmCodeEvent.OnCodeChange -> {
                            _mutableStateFlow.update { state ->
                                state.copy(
                                    code = Regex("[^0-9]").replace(event.value, ""),
                                    codeIsWrong = false
                                )
                            }
                            if (event.value.length == 4) sendCode()
                        }

                        ConfirmCodeEvent.MakeCallAgain -> makeCallAgain()

                        ConfirmCodeEvent.SkipAuthorization -> {
                            tokenService.skipAuthorization()
                            _navigationEvents.send(ConfirmCodeNavigationEvent.NavigateToHomeScreen)
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: ConfirmCodeEvent) = viewModelScope.launch {
        events.send(event)
    }

    fun onError(appError: AppError) {
        when (appError as ConfirmCodeAppError) {
            is ConfirmCodeAppError.WrongCodeError -> {
                /* TODO: ... */
            }

            is ConfirmCodeAppError.ServerError -> {
                /* TODO: ... */
            }

            is ConfirmCodeAppError.UnknownError -> {
                /* TODO: ... */
            }

            is ConfirmCodeAppError.MakeCallAgainError -> {
                /* TODO: ... */
            }
        }
    }

    private fun makeCallAgain() = viewModelScope.launch {
        repository.makeCallV2(phone).collectLatest { resourceState ->
            resourceState.onLoading { isLoading ->
                setLoading(isLoading)
            }.onSuccess { successCall ->
                when (successCall) {
                    true -> startTimer()
                    false -> _mutableStateFlow.update { state ->
                        state.copy(error = ConfirmCodeAppError.MakeCallAgainError())
                    }
                    else -> _mutableStateFlow.update { state ->
                        state.copy(error = ConfirmCodeAppError.UnknownError())
                    }
                }
            }.onError { error ->
                if (error is ConfirmCodeAppError) {
                    _mutableStateFlow.update { state ->
                        state.copy(error = error)
                    }
                }
            }
        }
    }

    private fun sendCode() = viewModelScope.launch {
        repository.sendCodeV2(
            phone, request = SendCodeRequest(
                code = stateFlow.value.code.toInt(),
                device = DeviceDTO(
                    id = null,
                    firebaseToken = "",
                    firmware = "",
                    version = ""
                )
            )
        ).collectLatest { resourceState ->
            resourceState.onLoading { isLoading ->
                setLoading(isLoading)
            }.onError { error ->
                if (error is ConfirmCodeAppError) {
                    _mutableStateFlow.update { state ->
                        state.copy(error = error)
                    }
                } else {
                    _mutableStateFlow.update { state ->
                        state.copy(error = ConfirmCodeAppError.UnknownError())
                    }
                }
            }.onSuccess { _ ->
                _navigationEvents.send(ConfirmCodeNavigationEvent.NavigateToHomeScreen)
            }
        }
    }

    private fun startTimer() = viewModelScope.launch {
        _mutableStateFlow.update { state -> state.copy(timer = 5) }
        do {
            delay(1000L)

            _mutableStateFlow.update { state ->
                state.copy(
                    timer = state.timer - 1,
                    canMakeCallAgain = state.timer == 0
                )
            }
        } while (stateFlow.value.timer >= 0)
    }

    private fun setLoading(value: Boolean) {
        _mutableStateFlow.update { state -> state.copy(isLoading = value) }
    }
}