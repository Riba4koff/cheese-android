package ru.antares.cheese_android.data.remote.api.main.addresses

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.api.main.addresses.request.CreateAddressRequest
import ru.antares.cheese_android.data.remote.api.main.addresses.request.UpdateAddressRequest
import ru.antares.cheese_android.data.remote.api.main.addresses.response.DeleteAddressResponse

/**
 * @author Pavel Rybakov
 * */

interface AddressesApi {
    @GET("addresses")
    suspend fun get(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sortDirection") sortDirection: String? = null,
        @Query("sortByColumn") sortByColumn: String? = null
    ): Response<CheeseNetworkResponse<Pagination<AddressDTO>>>

    @GET("addresses/{id}")
    suspend fun get(
        @Path("id") uuid: String
    ): Response<CheeseNetworkResponse<AddressDTO>>

    @POST("addresses")
    suspend fun create(
        @Body request: CreateAddressRequest
    ): Response<CheeseNetworkResponse<AddressDTO>>

    @DELETE("addresses/{id}")
    suspend fun delete(
        @Path("id") uuid: String
    ): Response<DeleteAddressResponse>

    @PUT("addresses/{id}")
    suspend fun update(
        @Path("id") uuid: String,
        @Body request: UpdateAddressRequest,
    ): Response<CheeseNetworkResponse<AddressDTO>>
}