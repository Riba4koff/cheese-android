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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.datastore.token.AuthorizedState.AUTHORIZED
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.User
import ru.antares.cheese_android.data.repository.auth.AuthorizationRepository
import ru.antares.cheese_android.data.repository.main.profile.ProfileRepository
import ru.antares.cheese_android.domain.errors.UIError

data class ProfileState(
    val surname: String = "",
    val name: String = "",
    val patronymic: String = "",
    val isLoading: Boolean = true,
    val profileLoaded: Boolean = false,
    val isAuthorized: Boolean = false,
    val error: UIError? = null
)


class ProfileViewModel(
    tokenService: ITokenService,
    private val authorizationRepository: AuthorizationRepository,
    private val profileRepository: ProfileRepository,
    private val userDataStore: IUserDataStore
) : ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<ProfileState> =
        MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<ProfileNavigationEvent> = Channel()
    val navigationEvents: Flow<ProfileNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch {
                userDataStore.user.collectLatest { user ->
                    _mutableStateFlow.update { state ->
                        state.copy(
                            surname = user.surname,
                            name = user.name,
                            patronymic = user.patronymic
                        )
                    }
                }
            }
            launch {
                tokenService.authorizedState.collectLatest { authState ->
                    _mutableStateFlow.update { state ->
                        state.copy(
                            isAuthorized = authState == AUTHORIZED
                        )
                    }
                }
            }
        }
    }

    private fun onError(uiError: UIError) = viewModelScope.launch {
        when (uiError as ProfileUIError) {
            is ProfileUIError.LoadProfileError -> loadProfileV2()
            is ProfileUIError.LogoutError -> logout()
        }
    }

    fun onEvent(event: ProfileEvent) = viewModelScope.launch {
        when (event) {
            is ProfileEvent.Retry -> onError(event.uiError)
            ProfileEvent.Logout -> logout()
            ProfileEvent.DeleteAccount -> {
                /* TODO: delete account logic */
            }
            ProfileEvent.LoadProfile -> loadProfileV2()
        }
    }

    fun onNavigationEvent(navigationEvent: ProfileNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(navigationEvent)
    }

    private suspend fun loadProfileV2() {
        if (state.value.isAuthorized && state.value.profileLoaded.not()) {
            Log.d("LOADING_PROFILE_VM", "loading")
            profileRepository.getV2().collect { resource ->
                resource.onSuccess { response ->
                    val emailAttachment = response.attachments.firstOrNull {
                        it.typeName == "EMAIL"
                    }.takeIf { it != null }

                    val phoneAttachment = response.attachments.firstOrNull {
                        it.typeName == "PHONE"
                    }.takeIf { it != null }

                    userDataStore.save(
                        user = User(
                            surname = response.surname,
                            name = response.firstname,
                            patronymic = response.patronymic,
                            email = emailAttachment?.value ?: "",
                            phone = phoneAttachment?.value ?: "",
                            birthday = response.birthday,
                            verifiedPhone = phoneAttachment?.verified ?: false,
                            verifiedEmail = phoneAttachment?.verified ?: false
                        )
                    ).onFailure { message ->
                        Log.d("SAVE_RROFILE", message)
                    }

                    withContext(Dispatchers.Main.immediate) {
                        _mutableStateFlow.update { state ->
                            state.copy(
                                error = null,
                                profileLoaded = true
                            )
                        }
                    }
                }.onError { error ->
                    withContext(Dispatchers.Main.immediate) {
                        _mutableStateFlow.update { state ->
                            state.copy(
                                error = error,
                                isLoading = false
                            )
                        }
                    }
                }.onLoading { isLoading ->
                    withContext(Dispatchers.Main.immediate) {
                        _mutableStateFlow.update { state -> state.copy(isLoading = isLoading) }
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main.immediate) {
                _mutableStateFlow.update { state ->
                    state.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    private suspend fun logout() {
        _mutableStateFlow.update { state -> state.copy(isLoading = true) }

        authorizationRepository.logout().onSuccess { successNetworkLogout ->
            if (successNetworkLogout == true) {
                _navigationEvents.send(ProfileNavigationEvent.Logout)
            }
        }.onFailure { message ->
            Log.d("LOGOUT_TAG", message)

            val error = ProfileUIError.LogoutError(message = "Не удалось выйти из аккаунта")
            _mutableStateFlow.update { state -> state.copy(error = error) }
        }
    }
}