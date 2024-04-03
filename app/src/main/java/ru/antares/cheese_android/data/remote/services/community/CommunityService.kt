package ru.antares.cheese_android.data.remote.services.community

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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
        size: Int? = null,
        page: Int? = null,
        sort: String? = null
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

