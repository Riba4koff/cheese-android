package ru.antares.cheese_android.data.remote.services.addresses

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.models.Response
import ru.antares.cheese_android.data.remote.services.addresses.dto.AddressDTO
import ru.antares.cheese_android.data.remote.services.addresses.request.CreateAddressRequest
import ru.antares.cheese_android.data.remote.services.addresses.request.UpdateAddressRequest
import ru.antares.cheese_android.data.remote.services.addresses.response.DeleteAddressResponse

interface AddressesService {
    @GET("addresses")
    suspend fun get(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sortDirection") sortDirection: String? = null,
        @Query("sortByColumn") sortByColumn: String? = null
    ): Response<Pagination<AddressDTO>>

    @POST("addresses")
    suspend fun create(
        @Header("Authorization") authorization: String,
        @Body request: CreateAddressRequest
    ): Response<AddressDTO>

    @DELETE("addresses/{id}")
    suspend fun delete(
        @Header("Authorization") authorization: String,
        @Path("id") uuid: String
    ): DeleteAddressResponse

    @GET("addresses/{id}")
    suspend fun receiveAddressByID(
        @Header("Authorization") authorization: String,
        @Path("id") uuid: String
    ): Response<AddressDTO>

    @PUT("addresses/{id}")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Body request: UpdateAddressRequest,
        @Path("id") uuid: String
    ): Response<AddressDTO>
}