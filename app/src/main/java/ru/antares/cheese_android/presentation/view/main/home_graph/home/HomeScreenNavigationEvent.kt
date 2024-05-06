package ru.antares.cheese_android.presentation.view.main.home_graph.home

/**
 * HomeScreenNavigationEvent.kt
 * @author Павел
 * Created by on 06.05.2024 at 21:37
 * Android studio
 */

sealed interface HomeScreenNavigationEvent {
    data class NavigateToRecommendation(val recommendationId: String) : HomeScreenNavigationEvent
    data class NavigateToActivity(val activityId: String) : HomeScreenNavigationEvent
    data class NavigateToPost(val postId: String) : HomeScreenNavigationEvent
}