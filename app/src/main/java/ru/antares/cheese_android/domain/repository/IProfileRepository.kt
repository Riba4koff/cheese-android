package ru.antares.cheese_android.domain.repository

import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.ProfileResponse

interface IProfileRepository {
    suspend fun get(): NetworkResponse<ProfileResponse>
    suspend fun update(request: UpdateProfileRequest): NetworkResponse<ProfileResponse>
    suspend fun delete(): NetworkResponse<Boolean?>
}