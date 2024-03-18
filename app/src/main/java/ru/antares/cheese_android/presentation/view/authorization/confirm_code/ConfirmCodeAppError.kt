package ru.antares.cheese_android.presentation.view.authorization.confirm_code

import ru.antares.cheese_android.domain.errors.AppError

sealed interface ConfirmCodeAppError : AppError {
    data class WrongCodeError(
        override val message: String = "Неверный код подтверждения"
    ) : ConfirmCodeAppError

    data class ServerError(
        override val message: String = "Что-то с сервером...\nПопробуйте позже"
    ) : ConfirmCodeAppError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ): ConfirmCodeAppError

    data class MakeCallAgainError(
        override val message: String = "Не удалось совершить звонок"
    ): ConfirmCodeAppError
}
