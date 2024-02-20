package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

import ru.antares.cheese_android.domain.errors.UIError

sealed interface ProfileUIError : UIError {
    data class UnauthorizedError(
        override val message: String = "Ошибка загрузки профиля\nВозможно, вы вышли с аккаунта\n на другом устройстве."
    ) : ProfileUIError

    data class LogoutError(
        override val message: String = "Произошла ошибка при выходе из аккаунта."
    ) : ProfileUIError

    data class LoadProfileError(
        override val message: String = "Не удалось загрузить профиль."
    ) : ProfileUIError
}