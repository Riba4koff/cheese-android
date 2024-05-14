package ru.antares.cheese_android.data.repository.main

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.api.main.orders.CreateOrderRequest
import ru.antares.cheese_android.data.remote.api.main.orders.CreateOrderResponse
import ru.antares.cheese_android.data.remote.api.main.orders.OrderDTO
import ru.antares.cheese_android.domain.repository.IOrdersRepository
import ru.antares.cheese_android.domain.repository.OrdersError
import ru.antares.cheese_android.domain.result.CheeseResult

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:42
 * Android Studio
 */

class OrdersRepository(

): IOrdersRepository {
    override suspend fun get(
        page: Int,
        size: Int,
        sortDirection: String?,
        sortByColumn: String?
    ): Flow<CheeseResult<OrdersError, List<OrderDTO>>> {
        TODO("Not yet implemented")
    }

    override suspend fun create(request: CreateOrderRequest): Flow<CheeseResult<OrdersError, CreateOrderResponse>> {
        TODO("Not yet implemented")
    }
}