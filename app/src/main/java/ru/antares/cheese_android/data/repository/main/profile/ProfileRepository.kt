package ru.antares.cheese_android.data.repository.main.profile

import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.main.profile.ProfileService
import ru.antares.cheese_android.data.remote.services.main.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.ProfileResponse
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.domain.repository.IProfileRepository

class ProfileRepository(
    private val profileService: ProfileService,
) : IProfileRepository {
    override suspend fun get(): NetworkResponse<ProfileResponse> = safeNetworkCall {
        profileService.get()
    }

    override suspend fun update(request: UpdateProfileRequest): NetworkResponse<ProfileResponse> =
        safeNetworkCall {
            profileService.update(request)
        }

    override suspend fun delete(): NetworkResponse<Boolean?> = safeNetworkCall {
        profileService.delete()
    }
}