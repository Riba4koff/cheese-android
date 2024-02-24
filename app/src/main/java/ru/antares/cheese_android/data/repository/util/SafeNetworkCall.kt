package ru.antares.cheese_android.data.repository.util

import android.util.Log
import retrofit2.Response
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.NetworkError
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.models.Pagination

/**
 * @author Pavel Rybakov
 * */

suspend fun <T> safeNetworkCall(block: suspend () -> Response<CheeseNetworkResponse<T>>): NetworkResponse<T> =
    try {
        val response = block()

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data == null) NetworkResponse.Error(
                    error = NetworkError(
                        message = "Не удалось загрузить данные",
                        code = response.code()
                    )
                )
                else NetworkResponse.Success(data)
            }

            else -> NetworkResponse.Error(
                error = NetworkError(
                    message = response.message(),
                    code = response.code()
                )
            )
        }
    } catch (e: Exception) {
        Log.d("SAFE_NETWORK_CALL", e.message.orEmpty())
        NetworkResponse.Error(error = NetworkError("Неизвестная ошибка"))
    }


suspend fun <T> safeNetworkCallWithPagination(
    block: suspend () -> Response<CheeseNetworkResponse<Pagination<T>>>
): NetworkResponse<Pagination<T>> = try {
    val response = block()

    when (response.code()) {
        in 200..299 -> {
            val data = response.body()?.data

            if (data == null) NetworkResponse.Error(
                error = NetworkError(
                    message = "Не удалось загрузить данные",
                    code = response.code()
                )
            )
            else NetworkResponse.Success(data)
        }

        else -> NetworkResponse.Error(
            error = NetworkError(
                message = response.message(),
                code = response.code()
            )
        )
    }
} catch (e: Exception) {
    NetworkResponse.Error(error = NetworkError("Неизвестная ошибка"))
}

