package ru.antares.cheese_android.data.repository.auth.responses

import ru.antares.cheese_android.data.repository.auth.models.SessionModel

data class SendCodeResponse(
    val token: String,
    val sessionModel: SessionModel
)
