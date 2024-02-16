package ru.antares.cheese_android.data.remote.services.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse

interface AuthorizationService {
    @GET("auth/phone/{phone}/call")
    suspend fun makeCall(
        @Path("phone") phone: String
    ): Response<CheeseNetworkResponse<Boolean?>>

    @POST("auth/phone/{phone}/call")
    suspend fun sendCode(
        @Path("phone") phone: String,
        @Body request: SendCodeRequest
    ): Response<CheeseNetworkResponse<SendCodeResponse>>

    @GET("auth/profile/logout")
    suspend fun logout(): Response<CheeseNetworkResponse<Boolean?>>
}