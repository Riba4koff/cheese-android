package ru.antares.cheese_android.data.remote.services.main.catalog

import retrofit2.Response
import retrofit2.http.GET
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.main.catalog.models.CategoryDTO

interface CatalogService {
    @GET("store/categories")
    suspend fun get(): Response<CheeseNetworkResponse<Pagination<CategoryDTO>>>
}