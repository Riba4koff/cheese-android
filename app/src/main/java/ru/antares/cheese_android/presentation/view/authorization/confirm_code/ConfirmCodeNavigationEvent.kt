package ru.antares.cheese_android.presentation.view.authorization.confirm_code

sealed interface ConfirmCodeNavigationEvent {
    data object NavigateToHomeScreen: ConfirmCodeNavigationEvent
}