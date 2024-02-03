package ru.antares.cheese_android.domain.repository

import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse

interface IAuthorizationRepository {
    suspend fun makeCall(phone: String): NetworkResponse<MakeCallResponse>
    suspend fun sendCode(phone: String, request: SendCodeRequest): NetworkResponse<SendCodeResponse>
    suspend fun logout(): NetworkResponse<Boolean?>
}