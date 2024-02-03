package ru.antares.cheese_android.presentation.view.authorization.confirm_code

sealed interface NavigationEvent {
    data object NavigateToHomeScreen: NavigationEvent
}