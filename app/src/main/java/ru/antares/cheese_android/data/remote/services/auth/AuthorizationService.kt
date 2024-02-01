package ru.antares.cheese_android.data.remote.services.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.data.remote.services.auth.response.SendCodeResponse

interface AuthorizationService {
    @GET("auth/phone/{phone}/call")
    suspend fun makeCall(
        @Path("phone") phone: String
    ): MakeCallResponse

    @POST("auth/phone/{phone}/call")
    suspend fun sendCode(
        @Path("phone") phone: String,
        @Body request: SendCodeRequest
    ): SendCodeResponse
}