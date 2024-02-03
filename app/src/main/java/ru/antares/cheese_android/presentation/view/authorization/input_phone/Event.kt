package ru.antares.cheese_android.presentation.view.authorization.input_phone

sealed interface Event {
    data class OnPhoneChange(val value: String): Event
    data object CloseAlertDialog: Event
    data object SkipAuthorization: Event
}