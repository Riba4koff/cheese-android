package ru.antares.cheese_android.presentation.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.AuthorizedState
import ru.antares.cheese_android.data.local.datastore.SecurityTokenService

class SplashScreenViewModel(
    private val tokenService: SecurityTokenService
): ViewModel() {
    private val _userAuthorizedState: MutableStateFlow<SplashScreenState> =
        MutableStateFlow(SplashScreenState.LOADING)
    val userAuthorizedState: StateFlow<SplashScreenState> =
        _userAuthorizedState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenService.authorized.map { authState ->
                if (authState == AuthorizedState.NOT_AUTHORIZED) SplashScreenState.NAVIGATE_TO_AUTHORIZE
                else SplashScreenState.NAVIGATE_TO_HOME_SCREEN
            }.collect(_userAuthorizedState)
        }
    }
}