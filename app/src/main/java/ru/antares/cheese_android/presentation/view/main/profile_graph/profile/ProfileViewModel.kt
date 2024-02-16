package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

import android.util.Log
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.datastore.token.AuthorizedState.AUTHORIZED
import ru.antares.cheese_android.data.local.datastore.token.AuthorizedState.NOT_AUTHORIZED
import ru.antares.cheese_android.data.local.datastore.token.AuthorizedState.SKIPPED
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.User
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.main.profile.response.ProfileResponse
import ru.antares.cheese_android.data.repository.auth.AuthorizationRepository
import ru.antares.cheese_android.data.repository.main.profile.ProfileRepository
import ru.antares.cheese_android.domain.errors.UIError

class ProfileViewModel(
    private val tokenService: ITokenService,
    private val authorizationRepository: AuthorizationRepository,
    private val profileRepository: ProfileRepository,
    private val userDataStore: IUserDataStore
) : ViewModel() {
    private val events: Channel<ProfileEvent> = Channel()

    private val _mutableStateFlow: MutableStateFlow<ProfileViewState> =
        MutableStateFlow(ProfileViewState.LoadingState())
    val state: StateFlow<ProfileViewState> = _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<ProfileNavigationEvent> = Channel()
    val navigationEvents: Flow<ProfileNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch {
                tokenService.authorizedState.collectLatest { authorized ->
                    when (authorized) {
                        AUTHORIZED -> {
                            loadProfile()
                            userDataStore.user.collectLatest { user ->
                                _mutableStateFlow.emit(
                                    ProfileViewState.AuthorizedState(
                                        surname = user.surname,
                                        name = user.name,
                                        patronymic = user.patronymic
                                    )
                                )
                            }
                        }

                        NOT_AUTHORIZED, SKIPPED -> _mutableStateFlow.emit(ProfileViewState.NonAuthorizedState())
                    }
                }
            }
            launch {
                events.receiveAsFlow().collectLatest { event ->
                    when (event) {
                        is ProfileEvent.Retry -> onError(event.uiError)
                        ProfileEvent.Logout -> logout()
                        ProfileEvent.DeleteAccount -> deleteAccount()
                    }
                }
            }
        }
    }

    private fun onError(uiError: UIError) = viewModelScope.launch {
        when (uiError as ProfileUIError) {
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

    private suspend fun loadProfile() {
        if (state.value !is ProfileViewState.LoadingState) {
            _mutableStateFlow.emit(ProfileViewState.LoadingState())
        }

        when (
            val response: NetworkResponse<ProfileResponse> =
                withContext(Dispatchers.IO) { profileRepository.get() }
        ) {
            is NetworkResponse.Error -> {
                val uiError =
                    ProfileUIError.LoadProfileError(message = "Не удалось загрузить профиль")
                ProfileViewState.ErrorState(uiError)
            }

            is NetworkResponse.Success -> {

                val emailAttachment = response.data.attachments.firstOrNull {
                    it.typeName == "EMAIL"
                }.takeIf { it != null }

                val phoneAttachment = response.data.attachments.firstOrNull {
                    it.typeName == "PHONE"
                }.takeIf { it != null }

                userDataStore.save(
                    user = User(
                        surname = response.data.surname,
                        name = response.data.firstname,
                        patronymic = response.data.patronymic,
                        email = emailAttachment?.value ?: "",
                        phone = phoneAttachment?.value ?: "",
                        birthday = response.data.birthday,
                        verifiedPhone = phoneAttachment?.verified ?: false,
                        verifiedEmail = phoneAttachment?.verified ?: false
                    )
                ).onFailure { message ->
                    Log.d("SAVE_RROFILE", message)
                }
            }
        }
    }

    private suspend fun logout() {
        _mutableStateFlow.emit(ProfileViewState.LoadingState())

        when (authorizationRepository.logout()) {
            is NetworkResponse.Error -> {
                val error = ProfileUIError.LogoutError(message = "Не удалось выйти из аккаунта")
                _mutableStateFlow.emit(ProfileViewState.ErrorState(error))
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