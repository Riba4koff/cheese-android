package ru.antares.cheese_android.data.remote.api.main.profile.response

data class ProfileResponse(
    val id: String,
    val surname: String,
    val firstname: String,
    val patronymic: String,
    val birthday: String,
    val attachments: List<Attachment>,
)

