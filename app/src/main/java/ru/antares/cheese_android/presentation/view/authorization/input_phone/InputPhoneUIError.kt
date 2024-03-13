package ru.antares.cheese_android.presentation.view.authorization.input_phone

import ru.antares.cheese_android.domain.errors.UIError

sealed interface InputPhoneUIError : UIError {
    data class MakeCallError(
        override val message: String = "Не удалось совершить звонок\nПопробуйте позже"
    ) : InputPhoneUIError

    data class ServerError(
        override val message: String = "Что-то с сервером...\nПопробуйте позже"
    ) : InputPhoneUIError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ) : InputPhoneUIError
}
