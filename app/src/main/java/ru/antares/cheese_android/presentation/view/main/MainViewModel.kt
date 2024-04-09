package ru.antares.cheese_android.presentation.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.antares.cheese_android.data.local.datastore.token.UserAuthorizationState
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.repository.main.CartRepository
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase

/**
 * MainViewModel.kt
 * @author Павел
 * Created by on 23.02.2024 at 17:19
 * Android studio
 */

class MainViewModel(
    private val cartRepository: CartRepository,
    private val getCartFlowUseCase: GetCartFlowUseCase,
    private val authDataStore: IAuthorizationDataStore
): ViewModel() {
    private val _countProductsInCart: MutableStateFlow<Int> = MutableStateFlow(0)
    val countProductsInCart: StateFlow<Int> = _countProductsInCart.asStateFlow()

    private val _isAuthorized: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAuthorized: StateFlow<Boolean> = _isAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                authDataStore.authorizedState.collectLatest { isAuthorizedState ->
                    _isAuthorized.emit(isAuthorizedState == UserAuthorizationState.AUTHORIZED)
                }
            }
            launch {
                cartRepository.updateLocalCart()
            }
            launch {
                getCartFlowUseCase.entitites.collectLatest { cart ->
                    _countProductsInCart.emit(cart.size)
                }
            }
        }
    }
}