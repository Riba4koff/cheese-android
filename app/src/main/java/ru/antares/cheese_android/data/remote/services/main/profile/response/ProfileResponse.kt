package ru.antares.cheese_android.data.remote.services.main.profile.response

data class ProfileResponse(
    val id: String,
    val surname: String,
    val firstname: String,
    val patronymic: String,
    val birthday: String,
    val attachments: List<Attachment>,
)

