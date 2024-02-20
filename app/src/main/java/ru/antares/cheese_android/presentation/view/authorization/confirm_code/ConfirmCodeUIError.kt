package ru.antares.cheese_android.presentation.view.authorization.confirm_code

import ru.antares.cheese_android.domain.errors.UIError

sealed interface ConfirmCodeUIError : UIError {
    data class WrongCodeError(
        override val message: String = "Неверный код подтверждения"
    ) : ConfirmCodeUIError

    data class ServerError(
        override val message: String = "Что-то с сервером...\nПопробуйте позже"
    ) : ConfirmCodeUIError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ): ConfirmCodeUIError

    data class MakeCallAgainError(
        override val message: String = "Не удалось совершить звонок"
    ): ConfirmCodeUIError
}
