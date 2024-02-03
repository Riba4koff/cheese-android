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
import ru.antares.cheese_android.data.local.datastore.SecurityTokenService
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.data.repository.auth.models.DeviceModel
import ru.antares.cheese_android.data.repository.auth.models.SessionModel
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository
import ru.antares.cheese_android.presentation.view.authorization.input_phone.ErrorState

class ConfirmCodeViewModel(
    private val phone: String,
    private val repository: IAuthorizationRepository,
    private val tokenService: SecurityTokenService
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<ConfirmCodeState> =
        MutableStateFlow(ConfirmCodeState())
    val stateFlow: StateFlow<ConfirmCodeState> =
        _mutableStateFlow.asStateFlow()

    private val events: Channel<Event> = Channel()

    private val _navigationEvents: Channel<NavigationEvent> = Channel()
    val navigationEvents: Flow<NavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch { startTimer() }
            launch {
                events.receiveAsFlow().collectLatest { event ->
                    when (event) {
                        Event.CloseAlertDialog -> _mutableStateFlow.update { state ->
                            state.copy(error = ErrorState())
                        }

                        is Event.OnCodeChange -> {
                            _mutableStateFlow.update { state ->
                                state.copy(
                                    code = Regex("[^0-9]").replace(event.value, ""),
                                    codeIsWrong = false
                                )
                            }
                            if (event.value.length == 4) sendCode()
                        }

                        Event.MakeCallAgain -> {
                            makeCallAgain()
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: Event) = viewModelScope.launch {
        events.send(event)
    }

    private fun makeCallAgain() = viewModelScope.launch {
        setLoading(true)

        val response = mockMakeCallAgain()

        delay(1000)

        when (response) {
            is NetworkResponse.Error -> {
                _mutableStateFlow.update { state ->
                    state.copy(
                        error = ErrorState(
                            isError = true,
                            message = response.message
                        )
                    )
                }
            }

            is NetworkResponse.Success -> {
                val successCall = response.data.data

                if (successCall) {
                    startTimer()
                } else _mutableStateFlow.update { state ->
                    state.copy(
                        error = ErrorState(
                            isError = true,
                            message = "Не удалось совершить звонок."
                        )
                    )
                }
            }
        }

        setLoading(false)
    }

    private fun sendCode() = viewModelScope.launch {
        setLoading(true)

        delay(1000L)

        when (val response = mockSendCodeCall()) {
            is NetworkResponse.Error -> {
                _mutableStateFlow.update { state ->
                    state.copy(
                        error = ErrorState(
                            isError = true,
                            message = response.message
                        ),
                        codeIsWrong = true
                    )
                }
            }

            is NetworkResponse.Success -> {
                tokenService.authorize(response.data.token)
                _navigationEvents.send(NavigationEvent.NavigateToHomeScreen)
            }
        }

        setLoading(false)
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

    private fun mockSendCodeCall(): NetworkResponse<SendCodeResponse> {
        return if (stateFlow.value.code == "6428") NetworkResponse.Success(
            data = SendCodeResponse(
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBdWRpZW5jZSIsImlzcyI6IkNoZWVzZU1vYmlsZSIsIlNFU1NJT05fSUQiOiI0NjJlOWZiYS1iOTJhLTQ4NjktYmIwMy1iOTZjNmNlN2Q2NzcifQ.xelhbKS7HsshMyw9S8Sz1UerPv47xiNHcyqzTN4eb0k",
                sessionModel = SessionModel(
                    authorizationType = "",
                    authorizedObject = "",
                    device = DeviceModel(
                        firebaseToken = "",
                        firmware = "",
                        id = null,
                        version = ""
                    ),
                    deviceId = "",
                    start = "",
                    finish = "",
                    id = "",
                    opened = false
                )
            )
        ) else NetworkResponse.Error("Неправильный код подтверждения")
    }

    private fun mockMakeCallAgain(): NetworkResponse<MakeCallResponse> {
        return NetworkResponse.Success(data = MakeCallResponse(false))
    }

    private fun setLoading(value: Boolean) {
        _mutableStateFlow.update { state -> state.copy(isLoading = value) }
    }
}