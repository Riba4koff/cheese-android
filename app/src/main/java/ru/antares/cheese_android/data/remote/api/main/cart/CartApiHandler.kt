package ru.antares.cheese_android.data.remote.api.main.cart

import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:06
 * Android Studio
 */

class CartApiHandler(
    private val service: CartApi
) {
    suspend fun get(): CheeseResult<CartError, BasketResponse> {
        return try {
            val response = service.get()

            if (response.code() == 401) return CheeseResult.Error(CartError.Unauthorized())

            when (response.code()) {
                in 200..299 -> {
                    val data = response.body()?.data

                    if (data != null) {
                        CheeseResult.Success(data)
                    } else {
                        CheeseResult.Error(CartError.ReceiveError())
                    }
                }

                in 500..599 -> {
                    CheeseResult.Error(CartError.ServerError())
                }

                else -> {
                    CheeseResult.Error(CartError.UnknownError())
                }
            }
        } catch (e: UnknownHostException) {
            CheeseResult.Error(CartError.NoInternetError())
        } catch (e: Exception) {
            e.printStackTrace()
            CheeseResult.Error(CartError.UnknownError())
        }
    }

    suspend fun update(request: UpdateCartRequest): CheeseResult<CartError, Boolean> = try {
        val response = service.update(request)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(CartError.UpdateError())
                }
            }

            in 500..599 -> {
                CheeseResult.Error(CartError.ServerError())
            }

            else -> {
                CheeseResult.Error(CartError.UnknownError())
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(CartError.NoInternetError())
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(CartError.UnknownError())
    }

    suspend fun delete(id: String): CheeseResult<CartError, Boolean> = try {
        val response = service.delete(id)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(CartError.DeleteProductError())
                }
            }

            in 500..599 -> {
                CheeseResult.Error(CartError.ServerError())
            }

            else -> {
                CheeseResult.Error(CartError.UnknownError())
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(CartError.NoInternetError())
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(CartError.UnknownError())
    }

    suspend fun clear(): CheeseResult<CartError, Boolean> = try {
        val response = service.clear()

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(CartError.ClearError())
                }
            }

            in 500..599 -> {
                CheeseResult.Error(CartError.ServerError())
            }

            else -> {
                CheeseResult.Error(CartError.UnknownError())
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(CartError.NoInternetError())
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(CartError.UnknownError())
    }
}