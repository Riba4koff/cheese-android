package ru.antares.cheese_android.data.remote.api.main.profile

import ru.antares.cheese_android.data.remote.api.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.api.main.profile.response.ProfileResponse
import ru.antares.cheese_android.domain.result.CheeseResult
import java.net.UnknownHostException

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 16:13
 * Android Studio
 */

class ProfileApiHandler(
    private val service: ProfileApi
) {
    suspend fun get(): CheeseResult<ProfileError, ProfileResponse> = try {
        val response = service.get()

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(ProfileError.ReceiveError)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(ProfileError.ServerError)
            }
            else -> {
                CheeseResult.Error(ProfileError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(ProfileError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(ProfileError.UnknownError)
    }

    suspend fun update(
        request: UpdateProfileRequest
    ): CheeseResult<ProfileError, ProfileResponse> = try {
        val response = service.update(request)

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data != null) {
                    CheeseResult.Success(data)
                } else {
                    CheeseResult.Error(ProfileError.UpdateError)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(ProfileError.ServerError)
            }
            else -> {
                CheeseResult.Error(ProfileError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(ProfileError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(ProfileError.UnknownError)
    }

    suspend fun delete(): CheeseResult<ProfileError, Boolean?> = try {
        val response = service.delete()

        when (response.code()) {
            in 200..299 -> {
                val data = response.body()?.data

                if (data == null) {
                    CheeseResult.Error(ProfileError.DeleteError)
                } else {
                    CheeseResult.Success(data)
                }
            }
            in 500..599 -> {
                CheeseResult.Error(ProfileError.ServerError)
            }
            else -> {
                CheeseResult.Error(ProfileError.UnknownError)
            }
        }
    } catch (e: UnknownHostException) {
        CheeseResult.Error(ProfileError.NoInternetError)
    } catch (e: Exception) {
        e.printStackTrace()
        CheeseResult.Error(ProfileError.UnknownError)
    }
}