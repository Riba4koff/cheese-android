package ru.antares.cheese_android.data.remote.services.profile.request

import ru.antares.cheese_android.data.remote.services.profile.response.Attachment

data class UpdateProfileRequest(
    val attachments: List<Attachment>? = null,
    val birthday: String? = null,
    val firstname: String? = null,
    val isBlocked: String? = null,
    val isDeleted: String? = null,
    val password: String? = null,
    val patronymic: String? = null,
    val surname: String? = null
)