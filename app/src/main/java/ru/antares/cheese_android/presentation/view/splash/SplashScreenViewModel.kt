package ru.antares.cheese_android.presentation.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.token.UserAuthorizationState
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.repository.main.ProfileRepository
import ru.antares.cheese_android.domain.ResourceState

class SplashScreenViewModel(
    private val authorizationDataStore: IAuthorizationDataStore
): ViewModel() {
    private val _userAuthorizedState: MutableStateFlow<SplashScreenState> =
        MutableStateFlow(SplashScreenState.LOADING)
    val userAuthorizedState: StateFlow<SplashScreenState> =
        _userAuthorizedState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                authorizationDataStore.authorize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBdWRpZW5jZSIsImlzcyI6IkNoZWVzZU1vYmlsZSIsIlNFU1NJT05fSUQiOiJjMDk2ODZlYi0wZmU2LTQwM2EtYjk5NS02NjJjZjhjZWE2NDUifQ.JHy8gPHaPE6atF5O3IYDAkoSRWIdKij7yTKNDp2GR7Y")
                authorizationDataStore.userAuthorizationState.map { authState ->
                    if (authState == UserAuthorizationState.AUTHORIZED || authState == UserAuthorizationState.SKIPPED) SplashScreenState.NAVIGATE_TO_HOME_SCREEN
                    else SplashScreenState.NAVIGATE_TO_AUTHORIZE
                }.collect(_userAuthorizedState)
            }
        }
    }
}