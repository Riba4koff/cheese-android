package ru.antares.cheese_android.presentation.view.main.community_graph.community

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 17:45
 * Android Studio
 */

sealed interface CommunityNavigationEvent {
    data class NavigateToActivity(val postID: String) : CommunityNavigationEvent
    data class NavigateToPost(val postID: String) : CommunityNavigationEvent
}