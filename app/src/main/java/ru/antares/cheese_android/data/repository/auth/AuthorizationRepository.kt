package ru.antares.cheese_android.data.repository.auth

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository

class AuthorizationRepository(
    private val authorizationService: AuthorizationService,
    private val tokenService: ITokenService,
) : IAuthorizationRepository {
    override suspend fun makeCall(
        phone: String,
    ): NetworkResponse<Boolean?> = safeNetworkCall {
        authorizationService.makeCall(phone)
    }

    override suspend fun sendCode(
        phone: String,
        request: SendCodeRequest,
    ): NetworkResponse<SendCodeResponse> = withContext(Dispatchers.IO) {
        val response = safeNetworkCall { authorizationService.sendCode(phone, request) }

        when (response) {
            is NetworkResponse.Error -> response
            is NetworkResponse.Success -> {
                Log.d("token", response.data.token)
                tokenService.authorize(response.data.token)
                response
            }
        }
    }

    override suspend fun logout(): NetworkResponse<Boolean?> = withContext(Dispatchers.IO) {
        when (val response = safeNetworkCall { authorizationService.logout() }) {
            is NetworkResponse.Error -> response
            is NetworkResponse.Success -> {
                tokenService.logout()
                response
            }
        }
    }
}