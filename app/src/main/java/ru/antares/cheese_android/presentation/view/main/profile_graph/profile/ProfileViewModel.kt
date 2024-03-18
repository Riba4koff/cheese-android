package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.optics.copy
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
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.User
import ru.antares.cheese_android.data.repository.auth.AuthorizationRepository
import ru.antares.cheese_android.data.repository.main.ProfileRepository
import ru.antares.cheese_android.domain.errors.AppError


class ProfileViewModel(
    private val tokenService: IAuthorizationDataStore,
    private val authorizationRepository: AuthorizationRepository,
    private val profileRepository: ProfileRepository,
    private val userDataStore: IUserDataStore,
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
                        state.copy {
                            ProfileState.surname set user.surname
                            ProfileState.name set user.name
                            ProfileState.patronymic set user.patronymic
                        }
                    }
                }
            }
            launch {
                tokenService.authorizedState.collectLatest { authState ->
                    _mutableStateFlow.update { state ->
                        state.copy {
                            ProfileState.isAuthorized set (authState == AUTHORIZED)
                        }
                    }
                }
            }
        }
    }


    fun onEvent(event: ProfileEvent) = viewModelScope.launch {
        when (event) {
            ProfileEvent.Logout -> logout()
            ProfileEvent.DeleteAccount -> {
                /* TODO: delete account logic */
            }

            ProfileEvent.LoadProfile -> loadProfileV2()
        }
    }

    fun onError(appError: AppError) = viewModelScope.launch {
        when (appError as ProfileAppError) {
            is ProfileAppError.LoadProfileError -> loadProfileV2()
            is ProfileAppError.LogoutError -> logout()
            is ProfileAppError.UnauthorizedError -> { tokenService.logout() }
        }
    }

    fun onNavigationEvent(navigationEvent: ProfileNavigationEvent) = viewModelScope.launch {
        _navigationEvents.send(navigationEvent)
    }

    private suspend fun loadProfileV2() {
        if (state.value.isAuthorized && state.value.profileLoaded.not()) {
            profileRepository.get().collect { resource ->
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
                            state.copy {
                                ProfileState.profileLoaded set true
                            }
                        }
                    }
                }.onError { error ->
                    withContext(Dispatchers.Main.immediate) {
                        _mutableStateFlow.update { state ->
                            state.copy {
                                ProfileState.error set error
                                ProfileState.isLoading set false
                            }
                        }
                    }
                }.onLoading { isLoading ->
                    withContext(Dispatchers.Main.immediate) {
                        _mutableStateFlow.update { state ->
                            state.copy {
                                ProfileState.isLoading set isLoading
                            }
                        }
                    }
                }
            }
        } else {
            withContext(Dispatchers.Main.immediate) {
                _mutableStateFlow.update { state ->
                    state.copy {
                        ProfileState.isLoading set false
                    }
                }
            }
        }
    }

    private suspend fun logout() {
        _mutableStateFlow.update { state ->
            state.copy {
                ProfileState.isLoading set true
            }
        }

        authorizationRepository.logout().onSuccess { successNetworkLogout ->
            if (successNetworkLogout == true) {
                _navigationEvents.send(ProfileNavigationEvent.Logout)
            }
        }.onFailure { error ->
            Log.d("LOGOUT_TAG", error.message)

            val uiError = ProfileAppError.LogoutError()

            _mutableStateFlow.update { state ->
                state.copy {
                    ProfileState.error set uiError
                    ProfileState.isLoading set false
                }
            }
        }
    }
}