package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.api.tickets.TicketDTO
import ru.antares.cheese_android.data.remote.api.tickets.TicketsError
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.domain.models.TicketModel
import ru.antares.cheese_android.domain.result.CheeseResult

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 13:30
 * Android Studio
 */

interface ITicketsRepository {
    suspend fun get(
        size: Int,
        page: Int,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): Flow<CheeseResult<TicketsError, Pagination<TicketModel>>>

    suspend fun pay(id: String): Flow<CheeseResult<TicketsError, TicketModel>>
}