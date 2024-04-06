package ru.antares.cheese_android.presentation.view.main.community_graph.post

/**
 * PostNavigationEvent.kt
 * @author Павел
 * Created by on 06.04.2024 at 23:44
 * Android studio
 */

sealed interface PostNavigationEvent {
    data object NavigateBack: PostNavigationEvent
    data class NavigateToProduct(val productID: String): PostNavigationEvent
}