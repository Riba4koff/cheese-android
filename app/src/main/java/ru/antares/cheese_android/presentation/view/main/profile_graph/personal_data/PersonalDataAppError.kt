package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import ru.antares.cheese_android.domain.errors.AppError

sealed interface PersonalDataAppError : AppError {
    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ) : PersonalDataAppError

    data class UpdateProfile(
        override val message: String = "Не удалось обновить профиль"
    ) : PersonalDataAppError
}