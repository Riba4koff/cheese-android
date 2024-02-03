package ru.antares.cheese_android.presentation.view.authorization.confirm_code

sealed interface Event {
    data class OnCodeChange(val value: String): Event
    data object CloseAlertDialog: Event
    data object MakeCallAgain: Event
}