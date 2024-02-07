package ru.antares.cheese_android.data.repository.util

import android.util.Log
import retrofit2.Response
import ru.antares.cheese_android.data.remote.models.CheeseNetworkResponse
import ru.antares.cheese_android.data.remote.models.NetworkResponse

suspend fun <T> safeNetworkCall(block: suspend () -> Response<CheeseNetworkResponse<T>>): NetworkResponse<T> = try {
    val response = block()

    when (response.code()) {
        in 200..299 -> {
            val data = response.body()?.data

            if (data == null) NetworkResponse.Error("Не удалось получить данные")
            else NetworkResponse.Success(data)
        }

        in 400..499 -> NetworkResponse.Error("Неверный запрос")
        in 500..599 -> NetworkResponse.Error("Ошибка сервера")
        else -> NetworkResponse.Error("Неизвестная ошибка")
    }
} catch (e: Exception) {
    NetworkResponse.Error("Неизвестная ошибка")
}