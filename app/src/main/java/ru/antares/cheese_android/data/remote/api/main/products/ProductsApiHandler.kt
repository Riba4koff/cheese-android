package ru.antares.cheese_android.data.remote.api.main.products

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:30
 * Android Studio
 */

class ProductsApiHandler(
    private val service: ProductsApi
) {
    suspend fun getByCategoryID(
        categoryID: String,
        page: Int? = null,
        size: Int? = null,
        sort: String? = null
    ): CheeseResult<ProductsError, Pagination<ProductDTO>> = try {
        val response = service.getByCategoryID(categoryID, page, size, sort)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(ProductsError.ReceiveError)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(ProductsError.ServerError)
            }
            else -> {
                CheeseResult.Error(ProductsError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(ProductsError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(ProductsError.UnknownError)
    }

    suspend fun getByProductID(productID: String): CheeseResult<ProductsError, ProductDTO> = try {
        val response = service.getProductByID(productID)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(ProductsError.ReceiveError)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(ProductsError.ServerError)
            }
            else -> {
                CheeseResult.Error(ProductsError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(ProductsError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(ProductsError.UnknownError)
    }
}