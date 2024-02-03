package ru.antares.cheese_android.data.repository.auth

import ru.antares.cheese_android.data.local.datastore.SecurityTokenService
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository

class AuthorizationRepository(
    private val authorizationService: AuthorizationService,
    private val securityTokenService: SecurityTokenService,
) : IAuthorizationRepository {
    override suspend fun makeCall(
        phone: String,
    ): NetworkResponse<MakeCallResponse> = safeNetworkCall {
        authorizationService.makeCall(phone)
    }

    override suspend fun sendCode(
        phone: String,
        request: SendCodeRequest,
    ): NetworkResponse<SendCodeResponse> {
        val response = safeNetworkCall { authorizationService.sendCode(phone, request) }

        when (response) {
            is NetworkResponse.Error -> return response
            is NetworkResponse.Success -> {
                securityTokenService.authorize(response.data.token)
                return response
            }
        }
    }

    override suspend fun logout(): NetworkResponse<Boolean?> {
        when (val response = safeNetworkCall { authorizationService.logout() }) {
            is NetworkResponse.Error -> return response
            is NetworkResponse.Success -> {
                securityTokenService.logout()
                return response
            }
        }
    }
}