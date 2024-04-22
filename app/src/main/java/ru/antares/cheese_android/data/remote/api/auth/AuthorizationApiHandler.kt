package ru.antares.cheese_android.data.remote.api.auth

import ru.antares.cheese_android.data.remote.api.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:59
 * Android Studio
 */

class AuthorizationApiHandler(
    private val service: AuthorizationApi
) {
    suspend fun call(
        phone: String
    ): CheeseResult<AuthorizationError, Boolean> = try {
        val response = service.call(phone)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data == null) {
                    CheeseResult.Error(AuthorizationError.LoadDataError)
                } else {
                    CheeseResult.Success(data)
                }
            }

            in 500..599 -> {
                CheeseResult.Error(AuthorizationError.ServerError)
            }

            else -> {
                CheeseResult.Error(AuthorizationError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(AuthorizationError.NoInternetConnectionError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(AuthorizationError.UnknownError)
    }

    suspend fun code(
        phone: String,
        request: SendCodeRequest
    ): CheeseResult<AuthorizationError, SendCodeResponse> = try {
        val response = service.code(phone, request)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data == null) {
                    CheeseResult.Error(AuthorizationError.LoadDataError)
                } else {
                    CheeseResult.Success(data)
                }
            }

            in 500..599 -> {
                CheeseResult.Error(AuthorizationError.ServerError)
            }

            else -> {
                CheeseResult.Error(AuthorizationError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(AuthorizationError.NoInternetConnectionError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(AuthorizationError.UnknownError)
    }

    suspend fun logout(): CheeseResult<AuthorizationError, Boolean> = try {
        val response = service.logout()

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data == null) {
                    CheeseResult.Error(AuthorizationError.LoadDataError)
                } else {
                    CheeseResult.Success(data)
                }
            }

            in 500..599 -> {
                CheeseResult.Error(AuthorizationError.ServerError)
            }

            else -> {
                CheeseResult.Error(AuthorizationError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(AuthorizationError.NoInternetConnectionError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(AuthorizationError.UnknownError)
    }
}