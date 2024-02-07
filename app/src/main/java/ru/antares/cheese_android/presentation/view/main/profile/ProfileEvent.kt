package ru.antares.cheese_android.presentation.view.main.profile

import ru.antares.cheese_android.domain.errors.UIError

sealed interface ProfileEvent {
    data object Logout: ProfileEvent
    data object DeleteAccount: ProfileEvent
    data class Retry(val uiError: UIError): ProfileEvent
}