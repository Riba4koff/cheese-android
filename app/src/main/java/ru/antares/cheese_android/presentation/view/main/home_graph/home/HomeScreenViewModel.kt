package ru.antares.cheese_android.presentation.view.main.home_graph.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * HomeScreenViewModel.kt
 * @author Павел
 * Created by on 06.05.2024 at 21:38
 * Android studio
 */

class HomeScreenViewModel(

): ViewModel() {
    private val _mutableStateFlow: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _mutableStateFlow.asStateFlow()

    private val _navigationEvents: Channel<HomeScreenNavigationEvent> = Channel()
    val navigationEvents: Flow<HomeScreenNavigationEvent> = _navigationEvents.receiveAsFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenRecommendationsEvent.AddToCart -> addRecommendationToCart(event.id, event.amount)
            is HomeScreenRecommendationsEvent.RemoveFromCart -> removeRecommendationFromCart(event.id, event.amount)
        }
    }

    fun onNavigationEvent(event: HomeScreenNavigationEvent) {
        viewModelScope.launch {
            _navigationEvents.send(event)
        }
    }

    private fun addRecommendationToCart(id: String, amount: Int) {
        viewModelScope.launch {
            /*TODO ... add recommendation to cart*/
        }
    }

    private fun removeRecommendationFromCart(id: String, amount: Int) {
        viewModelScope.launch {
            /*TODO ... remove recommendation from cart*/
        }
    }
}