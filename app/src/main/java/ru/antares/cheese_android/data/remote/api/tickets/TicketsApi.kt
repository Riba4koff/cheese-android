package ru.antares.cheese_android.data.remote.api.tickets

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination

/**
 * @author pavelrybakov
 * Created 22.04.2024 at 18:45
 * Android Studio
 */

interface TicketsApi {

    /**
     * GET
     * api/v1/activities/tickets
     *
     * Получение всех билетов пользователя
     *
     * @param size - размер страницы
     * @param page - номер страницы
     * @param sortDirection - направление сортировки
     * @param sortByColumn - столбец сортировки
     *
     * @return [Response]<[CheeseNetworkResponse]<[Pagination]<[TicketDTO]>>
     * */
    @GET("activities/tickets")
    suspend fun get(
        size: Int,
        page: Int,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): Response<Pagination<TicketDTO>>

    /**
     * POST
     * api/v1/activities/{id}/pay
     *
     * Покупка билета
     *
     * @param id - ID билета
     *
     * @return [Response]<[CheeseNetworkResponse]<[TicketDTO]>
     * */
    @POST("activities/{id}/pay")
    suspend fun pay(
        @Path("id") id: String
    ): Response<CheeseNetworkResponse<TicketDTO>>
}