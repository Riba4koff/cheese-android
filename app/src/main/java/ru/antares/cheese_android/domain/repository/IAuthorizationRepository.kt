package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse
import ru.antares.cheese_android.domain.ResourceState

interface IAuthorizationRepository {
    suspend fun makeCallV2(phone: String): Flow<ResourceState<Boolean?>>
    suspend fun sendCodeV2(phone: String, request: SendCodeRequest): Flow<ResourceState<Unit>>
    suspend fun logout(): NetworkResponse<Boolean?>
}