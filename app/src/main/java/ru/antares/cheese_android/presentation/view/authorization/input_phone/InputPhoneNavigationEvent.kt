package ru.antares.cheese_android.presentation.view.authorization.input_phone

sealed interface InputPhoneNavigationEvent {
    data object NavigateToHomeScreen : InputPhoneNavigationEvent
    data class NavigateToConfirmCode(val phone: String) : InputPhoneNavigationEvent
}