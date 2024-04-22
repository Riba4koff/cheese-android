package ru.antares.cheese_android.data.remote.api.main.catalog

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination

/**
 * @author Pavel Rybakov
 * */

interface CatalogApi {
    @GET("store/categories")
    suspend fun get(
        @Query("name") name: String? = null,
        @Query("position") position: Int? = null,
        @Query("parentID") parentID: String? = null,
        @Query("hasParent") hasParent: Boolean? = null,
        @Query("page") page: Int? = 0,
        @Query("pageSize") pageSize: Int? = 10,
        @Query("sortDirection") sortDirection: String? = null,
        @Query("sortByColumn") sortByColumn: String? = null
    ): Response<CheeseNetworkResponse<Pagination<CategoryDTO>>>
}