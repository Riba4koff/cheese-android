package ru.antares.cheese_android.data.repository.auth

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeUIError
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneUIError

class AuthorizationRepository(
    private val authorizationService: AuthorizationService,
    private val tokenService: ITokenService,
) : IAuthorizationRepository {
    override suspend fun makeCallV2(phone: String): Flow<ResourceState<Boolean?>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        val response = safeNetworkCall { authorizationService.makeCall(phone) }

        response.onFailure { error ->
            when (error.code) {
                in 500..599 -> {
                    emit(ResourceState.Error(error = InputPhoneUIError.ServerError()))
                    return@onFailure
                }
                else -> {
                    emit(ResourceState.Error(error = InputPhoneUIError.UnknownError()))
                    return@onFailure
                }
            }
        }.onSuccess { successCall ->
            emit(ResourceState.Success(successCall))
        }

        emit(ResourceState.Loading(isLoading = false))
    }
    override suspend fun sendCodeV2(
        phone: String,
        request: SendCodeRequest
    ): Flow<ResourceState<SendCodeResponse>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        val sendCodeResponse = safeNetworkCall { authorizationService.sendCode(phone, request) }

        sendCodeResponse.onFailure { error ->
            if (error.code in 500..599) {
                if (error.code == 500) emit(ResourceState.Error(error = ConfirmCodeUIError.WrongCodeError()))
                else emit(ResourceState.Error(error = ConfirmCodeUIError.ServerError()))
                return@onFailure
            } else {
                emit(ResourceState.Error(error = InputPhoneUIError.UnknownError()))
                return@onFailure
            }
        }.onSuccess { response ->
            emit(ResourceState.Success(response))
            return@onSuccess
        }

        emit(ResourceState.Loading(isLoading = false))
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