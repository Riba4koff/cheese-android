package ru.antares.cheese_android.presentation.view.main.profile_graph.tickets

import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.domain.models.TicketModel

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 14:24
 * Android Studio
 */

@Immutable
data class TicketsState(
    val loading: Boolean = true,
    val loadingNextPage: Boolean = false,
    val ticketsOfActivities: List<TicketModel>? = null,
    val currentPage: Int = 0,
    val pageSize: Int = 8,
    val endReached: Boolean = false
)