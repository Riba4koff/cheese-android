package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import ru.antares.cheese_android.domain.errors.UIError

sealed interface PersonalDataUIError : UIError {
    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ) : PersonalDataUIError

    data class UpdateProfile(
        override val message: String = "Не удалось обновить профиль"
    ) : PersonalDataUIError
}