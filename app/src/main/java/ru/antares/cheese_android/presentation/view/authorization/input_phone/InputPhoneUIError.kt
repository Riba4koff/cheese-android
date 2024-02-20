package ru.antares.cheese_android.presentation.view.authorization.input_phone

import ru.antares.cheese_android.domain.errors.UIError

sealed interface InputPhoneUIError : UIError {
    data class TooMuchCallsError(
        override val message: String = "Вы превысили\nдопустимое количество звонков.\nПопробуйте позже"
    ) : InputPhoneUIError

    data class ServerError(
        override val message: String = "Что-то с сервером...\nПопробуйте позже"
    ) : InputPhoneUIError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ) : InputPhoneUIError
}
