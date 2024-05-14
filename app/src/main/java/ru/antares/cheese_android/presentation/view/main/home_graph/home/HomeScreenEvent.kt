package ru.antares.cheese_android.presentation.view.main.home_graph.home

/**
 * HomeScreenEvent.kt
 * @author Павел
 * Created by on 06.05.2024 at 21:35
 * Android studio
 */

sealed interface HomeScreenEvent

sealed interface HomeScreenRecommendationsEvent: HomeScreenEvent {
    data class AddToCart(val id: String, val amount: Int) : HomeScreenRecommendationsEvent
    data class RemoveFromCart(val id: String, val amount: Int) : HomeScreenRecommendationsEvent
    data class LoadNextPage(val page: Int, val size: Int) : HomeScreenRecommendationsEvent
}