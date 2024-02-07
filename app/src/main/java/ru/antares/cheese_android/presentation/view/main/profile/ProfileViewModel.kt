package ru.antares.cheese_android.presentation.view.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.datastore.AuthorizedState.AUTHORIZED
import ru.antares.cheese_android.data.local.datastore.AuthorizedState.NOT_AUTHORIZED
import ru.antares.cheese_android.data.local.datastore.AuthorizedState.SKIPPED
import ru.antares.cheese_android.data.local.datastore.ITokenService
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.repository.auth.AuthorizationRepository
import ru.antares.cheese_android.data.repository.main.profile.ProfileRepository
import ru.antares.cheese_android.domain.errors.ProfileUIError
import ru.antares.cheese_android.domain.errors.UIError

class ProfileViewModel(
    private val tokenService: ITokenService,
    private val authorizationRepository: AuthorizationRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val events: Channel<ProfileEvent> = Channel()

    private val _mutableStateFlow: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.LoadingState)
    val state: StateFlow<ProfileScreenState> =
        combine(_mutableStateFlow, tokenService.authorizedState) { state, isAuthorized ->
            when (isAuthorized) {
                AUTHORIZED -> state
                NOT_AUTHORIZED -> ProfileScreenState.NonAuthorizedState
                SKIPPED -> ProfileScreenState.NonAuthorizedState
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileScreenState.LoadingState
        )

    private val _navigationEvents: Channel<ProfileNavigationEvent> = Channel()
    val navigationEvents: Flow<ProfileNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch {
                loadProfile()
            }
            launch {
                events.receiveAsFlow().collectLatest { event ->
                    when (event) {
                        is ProfileEvent.Retry -> retry(event.uiError)
                        ProfileEvent.Logout -> logout()
                        ProfileEvent.DeleteAccount -> deleteAccount()
                    }
                }
            }
        }
    }

    private fun retry(uiError: UIError) = viewModelScope.launch {
        when (uiError) {
            is ProfileUIError.LoadProfileError -> loadProfile()
            is ProfileUIError.LogoutError -> logout()
        }
    }

    fun onEvent(event: ProfileEvent) = viewModelScope.launch {
        events.send(event)
    }

    fun onNavigationEvent(navigationEvent: ProfileNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(navigationEvent)
    }

    private suspend fun loadProfile(): ProfileScreenState {
        if (state.value !is ProfileScreenState.LoadingState) {
            _mutableStateFlow.emit(ProfileScreenState.LoadingState)
        }
        val response = withContext(Dispatchers.IO) { profileRepository.get() }

        return when (response) {
            is NetworkResponse.Error -> {
                val uiError = ProfileUIError.LoadProfileError(
                    message = "Не удалось загрузить профиль"
                )
                ProfileScreenState.ErrorState(uiError)
            }

            is NetworkResponse.Success -> {
                ProfileScreenState.AuthorizedState(
                    surname = response.data.surname,
                    name = response.data.firstname,
                    patronymic = response.data.patronymic,
                )
            }
        }
    }

    private suspend fun logout() {
        _mutableStateFlow.emit(ProfileScreenState.LoadingState)

        when (authorizationRepository.logout()) {
            is NetworkResponse.Error -> {
                val error = ProfileUIError.LogoutError(message = "Не удалось выйти из аккаунта")
                _mutableStateFlow.emit(ProfileScreenState.ErrorState(error))
            }

            is NetworkResponse.Success -> {
                _navigationEvents.send(ProfileNavigationEvent.Logout)
            }
        }
    }

    private fun deleteAccount() = viewModelScope.launch {
        // TODO: - delete account logic
    }
}