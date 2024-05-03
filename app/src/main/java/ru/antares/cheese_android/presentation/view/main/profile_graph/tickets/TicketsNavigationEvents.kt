package ru.antares.cheese_android.presentation.view.main.profile_graph.tickets

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 15:26
 * Android Studio
 */

sealed interface TicketsNavigationEvents {
    data object NavigateBack: TicketsNavigationEvents
    data class NavigateToActivity(val postId: String): TicketsNavigationEvents
}