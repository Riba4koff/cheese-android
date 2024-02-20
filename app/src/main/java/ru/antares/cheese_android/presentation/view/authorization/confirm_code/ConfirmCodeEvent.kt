package ru.antares.cheese_android.presentation.view.authorization.confirm_code

sealed interface ConfirmCodeEvent {
    data class OnCodeChange(val value: String): ConfirmCodeEvent
    data object CloseAlertDialog: ConfirmCodeEvent
    data object MakeCallAgain: ConfirmCodeEvent
    data object SkipAuthorization : ConfirmCodeEvent
}