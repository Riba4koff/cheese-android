package ru.antares.cheese_android.data.remote.api.main.orders

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.domain.repository.OrdersError
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:48
 * Android Studio
 */

class OrdersApiHandler(
    private val api: OrdersApi
) {
    suspend fun get(
        page: Int,
        size: Int,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): CheeseResult<OrdersError, Pagination<OrderDTO>> = try {
        val response = api.create(page, size, sortDirection, sortByColumn)
        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data == null) {
                    CheeseResult.Error(OrdersError.GetError())
                } else {
                    CheeseResult.Success(data)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(OrdersError.ServerError())
            }
            else -> {
                CheeseResult.Error(OrdersError.UnknownError())
            }
        }
    } catch (uhe: UnknownHostException) {
        CheeseResult.Error(OrdersError.NoInternetError())
    } catch (e: Exception) {
        CheeseResult.Error(OrdersError.UnknownError())
    }

    suspend fun create(request: CreateOrderRequest): CheeseResult<OrdersError, CreateOrderResponse> = try {
        val response = api.create(request)
        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                if (data == null) {
                    CheeseResult.Error(OrdersError.CreateError())
                } else {
                    CheeseResult.Success(data)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(OrdersError.ServerError())
            }
            else -> {
                CheeseResult.Error(OrdersError.UnknownError())
            }
        }
    } catch (uhe: UnknownHostException) {
        CheeseResult.Error(OrdersError.NoInternetError())
    } catch (e: Exception) {
        CheeseResult.Error(OrdersError.UnknownError())
    }
}