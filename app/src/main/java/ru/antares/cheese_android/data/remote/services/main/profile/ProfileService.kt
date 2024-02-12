package ru.antares.cheese_android.data.remote.services.main.profile

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.ProfileResponse

interface ProfileService {
    @GET("auth/profile")
    suspend fun get(): Response<CheeseNetworkResponse<ProfileResponse>>

    @PUT("auth/profile")
    suspend fun update(
        @Body request: UpdateProfileRequest
    ): Response<CheeseNetworkResponse<ProfileResponse>>

    @DELETE("auth/profile")
    suspend fun delete(): Response<CheeseNetworkResponse<Boolean?>>
}