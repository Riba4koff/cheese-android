package ru.antares.cheese_android.data.remote.services.profile

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import ru.antares.cheese_android.data.remote.Response
import ru.antares.cheese_android.data.remote.services.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.profile.response.ProfileResponse

interface ProfileService {
    @GET("auth/profile")
    suspend fun get(
        @Header("Authorization") authorization: String
    ): Response<ProfileResponse>

    @PUT("auth/profile")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Body request: UpdateProfileRequest
    ): Response<ProfileResponse>

    @DELETE("auth/profile")
    suspend fun delete(
        @Header("Authorization") bearerToken: String
    )
}