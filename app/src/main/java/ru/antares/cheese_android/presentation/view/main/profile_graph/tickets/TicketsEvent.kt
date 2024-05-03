package ru.antares.cheese_android.presentation.view.main.profile_graph.tickets

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 14:25
 * Android Studio
 */

sealed interface TicketsEvent {
    data class LoadNextPage(val page: Int, val size: Int) : TicketsEvent
    data class OnClickTicket(val postId: String) : TicketsEvent
}