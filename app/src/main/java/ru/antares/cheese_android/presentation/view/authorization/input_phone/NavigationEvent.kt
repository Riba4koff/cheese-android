package ru.antares.cheese_android.presentation.view.authorization.input_phone

sealed interface NavigationEvent {
    data class NavigateToConfirmCode(val phone: String): NavigationEvent
}