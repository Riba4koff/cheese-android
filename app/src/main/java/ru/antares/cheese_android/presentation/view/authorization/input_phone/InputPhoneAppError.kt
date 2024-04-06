package ru.antares.cheese_android.presentation.view.authorization.input_phone

import ru.antares.cheese_android.domain.errors.AppError

sealed interface InputPhoneAppError : AppError {
    data class MakeCallError(
        override val message: String = "Не удалось совершить звонок\nПопробуйте позже"
    ) : InputPhoneAppError

    data class ServerError(
        override val message: String = "Что-то с сервером...\nПопробуйте позже"
    ) : InputPhoneAppError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ) : InputPhoneAppError
}