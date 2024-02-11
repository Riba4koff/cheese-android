package ru.antares.cheese_android.data.repository.main.profile

import android.util.Log
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.User
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.main.profile.ProfileService
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.ProfileResponse
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.repository.IProfileRepository

class ProfileRepository(
    private val profileService: ProfileService,
    private val userDS: IUserDataStore
) : IProfileRepository {
    override suspend fun get(): NetworkResponse<ProfileResponse> = safeNetworkCall {
        profileService.get()
    }

    override suspend fun update(request: UpdateProfileRequest): NetworkResponse<ProfileResponse> {
        return safeNetworkCall {
            Log.d("UPDATE_PROFILE_REQUEST", request.toString())
            profileService.update(request)
        }.onSuccess { response ->
            val emailAttachment = response.attachments.firstOrNull {
                it.typeName == "EMAIL"
            }.takeIf { it != null }

            val phoneAttachment = response.attachments.firstOrNull {
                it.typeName == "PHONE"
            }.takeIf { it != null }

            userDS.save(
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
        }
    }

    override suspend fun delete(): NetworkResponse<Boolean?> = safeNetworkCall {
        profileService.delete()
    }
}