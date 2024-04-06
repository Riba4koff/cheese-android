package ru.antares.cheese_android.presentation.view.main.community_graph.activity

/**
 * CommunityDetailNavigationEven.kt
 * @author Павел
 * Created by on 06.04.2024 at 19:57
 * Android studio
 */

sealed interface ActivityNavigationEvent {
    data object NavigateBack : ActivityNavigationEvent
    data class NavigateToProduct(val productID: String) : ActivityNavigationEvent
}