package ru.antares.cheese_android.data.remote.api.main.orders

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination

/**
 * @author pavelrybakov
 * Created 06.05.2024 at 13:43
 * Android Studio
 */

interface OrdersApi {

    /**
     * GET
     * api/v1/orders
     * Получение заказов пользователя
     *
     * @param [page] Page number
     * @param [size] Size of the page
     * @param [sortDirection] ASC/DESC
     * @param [sortByColumn] Column name
     *
     * @return [Response]<[CheeseNetworkResponse]<[List]<[OrderDTO]>>
     * */
    @GET("orders")
    suspend fun create(
        page: Int,
        size: Int,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): Response<CheeseNetworkResponse<Pagination<OrderDTO>>>

    /**
     * POST
     * api/v1/orders
     * Создание заказа пользователя
     *
     * @param [request] Create order request
     *
     * @return [Response]<[CheeseNetworkResponse]<[OrderDTO]>
     * */
    @POST("orders")
    suspend fun create(
        request: CreateOrderRequest
    ): Response<CheeseNetworkResponse<CreateOrderResponse>>
}