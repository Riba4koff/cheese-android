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

class SplashScreenViewModel(
    private val authorizationDataStore: IAuthorizationDataStore,
): ViewModel() {
    private val _userAuthorizedState: MutableStateFlow<SplashScreenState> =
        MutableStateFlow(SplashScreenState.LOADING)
    val userAuthorizedState: StateFlow<SplashScreenState> =
        _userAuthorizedState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                authorizationDataStore.authorizedState.map { authState ->
                    if (authState == UserAuthorizationState.AUTHORIZED || authState == UserAuthorizationState.SKIPPED) SplashScreenState.NAVIGATE_TO_HOME_SCREEN
                    else SplashScreenState.NAVIGATE_TO_AUTHORIZE
                }.collect(_userAuthorizedState)
            }
        }
    }
}