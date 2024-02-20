package ru.antares.cheese_android.data.remote.services.main.products

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.antares.cheese_android.data.remote.dto.ProductDTO
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination

/**
 * @author Pavel Rybakov
 * */

interface ProductsService {
    /**
     * GET: List of products
     *
     * @param [categoryID] Get products by category id (UUID)
     * @param [page] Page number
     * @param [size] Size of the page
     * @param [sortByColumn] ASC/DESC
     *
     * @return [Response]<[CheeseNetworkResponse]<[Pagination]<[ProductDTO]>>>
     * */
    @GET("store/categories/{id}/products")
    suspend fun getByCategoryID(
        @Path("id") categoryID: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("sortByColumn") sortByColumn: String? = null
    ): Response<CheeseNetworkResponse<Pagination<ProductDTO>>>

    /**
     * GET: Product by id (UUID)
     *
     * @param [productID] product id
     *
     * @return [Response]<[CheeseNetworkResponse]<[ProductDTO]>>
     * */
    @GET("store/products/products/{id}")
    suspend fun getProductByID(
        @Path("id") productID: String
    ): Response<CheeseNetworkResponse<ProductDTO>>


}