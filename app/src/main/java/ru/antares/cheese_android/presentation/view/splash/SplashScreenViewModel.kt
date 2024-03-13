package ru.antares.cheese_android.presentation.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.token.AuthorizedState
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore

class SplashScreenViewModel(
    private val tokenService: IAuthorizationDataStore
): ViewModel() {
    private val _userAuthorizedState: MutableStateFlow<SplashScreenState> =
        MutableStateFlow(SplashScreenState.LOADING)
    val userAuthorizedState: StateFlow<SplashScreenState> =
        _userAuthorizedState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenService.authorizedState.map { authState ->
                if (authState == AuthorizedState.AUTHORIZED || authState == AuthorizedState.SKIPPED) SplashScreenState.NAVIGATE_TO_HOME_SCREEN
                else SplashScreenState.NAVIGATE_TO_AUTHORIZE
            }.collect(_userAuthorizedState)
        }
    }
}