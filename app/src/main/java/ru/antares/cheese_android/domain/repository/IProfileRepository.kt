package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.api.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.api.main.profile.response.ProfileResponse
import ru.antares.cheese_android.domain.ResourceState

interface IProfileRepository {
    fun get(): Flow<ResourceState<ProfileResponse>>
    suspend fun updateV2(request: UpdateProfileRequest): Flow<ResourceState<Unit>>
    suspend fun delete(): NetworkResponse<Boolean?>
}