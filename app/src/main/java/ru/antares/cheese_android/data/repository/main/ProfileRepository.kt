package ru.antares.cheese_android.data.repository.main

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.User
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.main.profile.ProfileService
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.ProfileResponse
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.ResourceState
import ru.antares.cheese_android.domain.repository.IProfileRepository
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataAppError
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileAppError

/**
 * @author Pavel Rybakov
 * */

class ProfileRepository(
    private val profileService: ProfileService,
    private val userDS: IUserDataStore,
) : IProfileRepository {
    override fun get(): Flow<ResourceState<ProfileResponse>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        val response = profileService.get()

        if (response.isSuccessful) {
            val data = response.body()?.data

            if (data == null) {
                emit(ResourceState.Error(error = ProfileAppError.LoadProfileError()))
                return@flow
            } else {
                emit(ResourceState.Success(data = data))
                emit(ResourceState.Loading(isLoading = false))
            }
        } else {
            if (response.code() == 401) {
                emit(ResourceState.Error(ProfileAppError.UnauthorizedError()))
                return@flow
            } else {
                emit(ResourceState.Error(error = ProfileAppError.LoadProfileError()))
                return@flow
            }
        }
    }

    override suspend fun updateV2(request: UpdateProfileRequest): Flow<ResourceState<Unit>> = flow {
        emit(ResourceState.Loading(isLoading = true))

        val networkProfileUpdateResponse = safeNetworkCall {
            profileService.update(request)
        }

        networkProfileUpdateResponse.onSuccess { response ->
            val emailAttachment = response.attachments.firstOrNull {
                it.typeName == "EMAIL"
            }.takeIf { it != null }

            val phoneAttachment = response.attachments.firstOrNull {
                it.typeName == "PHONE"
            }.takeIf { it != null }

            val localUpdateProfileResult = userDS.save(
                user = User(
                    surname = response.surname,
                    name = response.firstname,
                    patronymic = response.patronymic,
                    email = emailAttachment?.value ?: "",
                    phone = phoneAttachment?.value ?: "",
                    birthday = response.birthday,
                    verifiedPhone = phoneAttachment?.verified ?: false,
                    verifiedEmail = emailAttachment?.verified ?: false
                )
            )

            localUpdateProfileResult.onFailure { errorMessage ->
                Log.d("UPDATE_PROFILE_ERROR", errorMessage)
                emit(ResourceState.Error(PersonalDataAppError.UpdateProfile()))
            }.onSuccess {
                emit(ResourceState.Success(Unit))
            }
        }.onFailure { error ->
            Log.d("UPDATE_PROFILE_ERROR", error.message)
            emit(ResourceState.Error(PersonalDataAppError.UnknownError()))
        }
    }

    override suspend fun delete(): NetworkResponse<Boolean?> = safeNetworkCall {
        profileService.delete()
    }
}