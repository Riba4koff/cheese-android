package ru.antares.cheese_android.data.repository.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.remote.api.tickets.TicketDTO
import ru.antares.cheese_android.data.remote.api.tickets.TicketsApiHandler
import ru.antares.cheese_android.data.remote.api.tickets.TicketsError
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.models.map
import ru.antares.cheese_android.domain.models.TicketModel
import ru.antares.cheese_android.domain.repository.ITicketsRepository
import ru.antares.cheese_android.domain.result.CheeseResult

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 13:34
 * Android Studio
 */

class TicketsRepository(
    private val handler: TicketsApiHandler
): ITicketsRepository {
    override suspend fun get(
        size: Int,
        page: Int,
        sortDirection: String?,
        sortByColumn: String?
    ): Flow<CheeseResult<TicketsError, Pagination<TicketModel>>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get(size, page, sortDirection, sortByColumn).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { data: Pagination<TicketDTO> ->
            val mapped = data.map { it.toModel() }

            emit(CheeseResult.Success(mapped))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }

    override suspend fun pay(
        id: String
    ): Flow<CheeseResult<TicketsError, TicketModel>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.pay(id).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { data: TicketDTO ->
            val mapped = data.toModel()

            emit(CheeseResult.Success(mapped))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }
}