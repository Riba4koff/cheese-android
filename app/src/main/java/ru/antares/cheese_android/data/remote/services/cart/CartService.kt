package ru.antares.cheese_android.data.remote.services.cart

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse

/**
 * CartService.kt
 * @author Павел
 * Created by on 23.02.2024 at 14:34
 * Android studio
 */

interface CartService {

    /**
     * GET: cart of user
     *
     * @return [Response]<[CheeseNetworkResponse]<[BasketResponse]>>
     */
    @GET("basket")
    suspend fun get(): Response<CheeseNetworkResponse<BasketResponse>>

    /**
     * POST: update product in cart
     *
     * @param [request]
     *
     * @return [Response]<[CheeseNetworkResponse]<[Boolean]>>
     * */
    @POST("basket")
    suspend fun update(
        @Body request: UpdateCartRequest
    ): Response<CheeseNetworkResponse<Boolean>>

    /**
     * DELETE: remove product with all count from cart
     *
     * @param productID
     *
     * @return [Response]<[CheeseNetworkResponse]<[Boolean]>>
     * */
    @DELETE("basket/products/{productID}")
    suspend fun delete(
        @Path("productID") productID: String
    ): Response<CheeseNetworkResponse<Boolean>>

    /**
     * DELETE: clear cart
     *
     * @return [Response]<[CheeseNetworkResponse]<[Boolean]>>
     * */
    @DELETE("basket")
    suspend fun clear(): Response<CheeseNetworkResponse<Boolean>>
}