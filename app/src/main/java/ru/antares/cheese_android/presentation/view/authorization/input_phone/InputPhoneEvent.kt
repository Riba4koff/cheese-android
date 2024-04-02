package ru.antares.cheese_android.presentation.view.authorization.input_phone

sealed interface InputPhoneEvent {
    data class OnPhoneChange(val value: String): InputPhoneEvent
    data object SkipAuthorization: InputPhoneEvent
}