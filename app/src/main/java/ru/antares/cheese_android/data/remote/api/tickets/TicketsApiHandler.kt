package ru.antares.cheese_android.data.remote.api.tickets

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.domain.result.CheeseResult

/**
 * @author pavelrybakov
 * Created 22.04.2024 at 18:53
 * Android Studio
 */

class TicketsApiHandler(
    private val service: TicketsApi
) {
    suspend fun get(
        size: Int,
        page: Int,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): CheeseResult<TicketsError, Pagination<TicketDTO>> {
        return try {
            val response = service.get(
                size = size,
                page = page,
                sortDirection = sortDirection,
                sortByColumn = sortByColumn
            )

            if (response.code() == 401) return CheeseResult.Error(TicketsError.Unauthorized())

            if (response.code() in 500..599) return CheeseResult.Error(TicketsError.ServerError())

            if (response.code() == 0) return CheeseResult.Error(TicketsError.NoInternetError())

            if (response.code() in 200..299) {
                val data = response.body()

                if (data != null) CheeseResult.Success(data)
                else CheeseResult.Error(TicketsError.ReceiveError())
            } else {
                CheeseResult.Error(TicketsError.UnknownError())
            }
        } catch (e: Exception) {
            CheeseResult.Error(TicketsError.UnknownError())
        }
    }

    suspend fun pay(
        id: String
    ): CheeseResult<TicketsError, TicketDTO> {
        return try {
            val response = service.pay(id = id)

            if (response.code() == 401) return CheeseResult.Error(TicketsError.Unauthorized())

            if (response.code() in 500..599) return CheeseResult.Error(TicketsError.ServerError())

            if (response.code() == 0) return CheeseResult.Error(TicketsError.NoInternetError())

            if (response.code() in 200..299) {
                val data = response.body()?.data

                if (data != null) CheeseResult.Success(data)
                else CheeseResult.Error(TicketsError.ReceiveError())
            } else {
                CheeseResult.Error(TicketsError.UnknownError())
            }
        } catch (e: Exception) {
            CheeseResult.Error(TicketsError.UnknownError())
        }
    }
}