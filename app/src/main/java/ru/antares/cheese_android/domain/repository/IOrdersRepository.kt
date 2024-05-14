package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.api.main.orders.CreateOrderRequest
import ru.antares.cheese_android.data.remote.api.main.orders.CreateOrderResponse
import ru.antares.cheese_android.data.remote.api.main.orders.OrderDTO
import ru.antares.cheese_android.domain.result.CheeseError
import ru.antares.cheese_android.domain.result.CheeseResult

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:22
 * Android Studio
 */

interface IOrdersRepository {
    suspend fun get(
        page: Int,
        size: Int,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): Flow<CheeseResult<OrdersError, List<OrderDTO>>>
    suspend fun create(request: CreateOrderRequest): Flow<CheeseResult<OrdersError, CreateOrderResponse>>
}

sealed interface OrdersError: CheeseError {
    val message: String
    data class NoInternetError(
        override val message: String = "Отсутствует подключение к интернету"
    ): OrdersError

    data class ServerError(
        override val message: String = "Произошла ошибка на сервере"
    ): OrdersError

    data class GetError(
        override val message: String = "Не удалось получить заказы"
    ): OrdersError

    data class CreateError(
        override val message: String = "Не удалось создать заказ"
    ): OrdersError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка в приложении"
    ): OrdersError
}