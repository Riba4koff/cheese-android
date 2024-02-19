package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.ProfileResponse
import ru.antares.cheese_android.domain.ResourceState

interface IProfileRepository {
    suspend fun get(): NetworkResponse<ProfileResponse>
    fun getV2(): Flow<ResourceState<ProfileResponse>>
    suspend fun update(request: UpdateProfileRequest): NetworkResponse<ProfileResponse>
    suspend fun delete(): NetworkResponse<Boolean?>
}