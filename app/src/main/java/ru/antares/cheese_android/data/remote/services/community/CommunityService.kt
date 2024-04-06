package ru.antares.cheese_android.data.remote.services.community

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.community.dto.PostDTO

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:05
 * Android Studio
 */

interface CommunityService {
    /**
     * GET
     * api/v1/posts
     *
     * @param [size] Size of the page
     * @param [page] Page number
     * @param [sort] ASC/DESC
     *
     * @return [Response]<[CheeseNetworkResponse]<[Pagination]<[PostDTO]>>
     * */
    @GET("posts")
    suspend fun get(
        @Query("size") size: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sortByColumn") sort: String? = null,
        @Query("hasActivity") hasActivity: Boolean? = null
    ): Response<CheeseNetworkResponse<Pagination<PostDTO>>>

    /**
     * GET
     * api/v1/posts
     *
     * @param [id] Post id
     *
     * @return [Response]<[CheeseNetworkResponse]<[PostDTO]>
     * */
    @GET("posts/{id}")
    suspend fun get(
        @Path("id") id: String
    ): Response<CheeseNetworkResponse<PostDTO>>
}

