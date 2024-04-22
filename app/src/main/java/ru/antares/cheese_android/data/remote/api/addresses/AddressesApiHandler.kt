package ru.antares.cheese_android.data.remote.api.addresses

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.api.addresses.request.CreateAddressRequest
import ru.antares.cheese_android.data.remote.api.addresses.request.UpdateAddressRequest
import ru.antares.cheese_android.data.remote.api.addresses.response.DeleteAddressResponse
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 15:16
 * Android Studio
 */

class AddressesApiHandler(
    private val service: AddressesApi
) {
    suspend fun get(
        page: Int,
        size: Int,
        sortDirection: String? = null,
        sortByColumn: String? = null
    ): CheeseResult<AddressesError, Pagination<AddressDTO>> = try {
        val response = service.get(
            page = page,
            size = size,
            sortDirection = sortDirection,
            sortByColumn = sortByColumn
        )

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(AddressesError.ReceiveError)
                }
            }

            in 500..599 -> {
                CheeseResult.Error(AddressesError.ServerError)
            }

            else -> CheeseResult.Error(AddressesError.UnknownError)
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(AddressesError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(AddressesError.UnknownError)
    }

    suspend fun get(id: String): CheeseResult<AddressesError, AddressDTO> = try {
        val response = service.get(uuid = id)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(AddressesError.ReceiveError)
                }
            }

            in 500..599 -> CheeseResult.Error(AddressesError.ServerError)

            else -> CheeseResult.Error(AddressesError.UnknownError)
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(AddressesError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(AddressesError.UnknownError)
    }

    suspend fun create(request: CreateAddressRequest): CheeseResult<AddressesError, AddressDTO> =
        try {
            val response = service.create(request)

            when (response.code()) {
                in 200..299 -> {
                    val data = response.body()?.data
                    if (data != null) {
                        CheeseResult.Success(data)
                    } else {
                        CheeseResult.Error(AddressesError.CreateError)
                    }
                }

                in 500..599 -> CheeseResult.Error(AddressesError.ServerError)

                else -> CheeseResult.Error(AddressesError.UnknownError)
            }
        } catch (e: UnknownHostException) {
            CheeseResult.Error(AddressesError.NoInternetError)
        } catch (e: Exception) {
            e.printStackTrace()
            CheeseResult.Error(AddressesError.UnknownError)
        }

    suspend fun delete(id: String): CheeseResult<AddressesError, DeleteAddressResponse> = try {
        val response = service.delete(id)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(AddressesError.DeleteError)
                }
            }

            in 500..599 -> CheeseResult.Error(AddressesError.ServerError)

            else -> CheeseResult.Error(AddressesError.UnknownError)
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(AddressesError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(AddressesError.UnknownError)
    }

    suspend fun update(
        id: String,
        request: UpdateAddressRequest
    ): CheeseResult<AddressesError, AddressDTO> = try {
        val response = service.update(
            uuid = id,
            request = request
        )

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data
                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(AddressesError.UpdateError)
                }
            }

            in 500..599 -> CheeseResult.Error(AddressesError.ServerError)
            else -> CheeseResult.Error(AddressesError.UnknownError)
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(AddressesError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(AddressesError.UnknownError)
    }
}