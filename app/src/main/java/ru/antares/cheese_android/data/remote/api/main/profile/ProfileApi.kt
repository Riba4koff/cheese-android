package ru.antares.cheese_android.data.remote.api.main.profile

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.api.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.api.main.profile.response.ProfileResponse

/**
 * @author Pavel Rybakov
 * */

interface ProfileApi {
    /**
     * GET
     * auth/profile
     *
     * @return [Response]<[CheeseNetworkResponse]<[ProfileResponse]>
     * */
    @GET("auth/profile")
    suspend fun get(): Response<CheeseNetworkResponse<ProfileResponse>>

    /**
     * PUT
     * auth/profile
     *
     * @param [request] Update profile request
     *
     * @return [Response]<[CheeseNetworkResponse]<[ProfileResponse]>
     * */
    @PUT("auth/profile")
    suspend fun update(
        @Body request: UpdateProfileRequest
    ): Response<CheeseNetworkResponse<ProfileResponse>>

    /**
     * DELETE
     * auth/profile
     *
     * @return [Response]<[CheeseNetworkResponse]<[Boolean]?>>
     * */
    @DELETE("auth/profile")
    suspend fun delete(): Response<CheeseNetworkResponse<Boolean?>>
}