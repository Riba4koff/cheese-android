package ru.antares.cheese_android.presentation.view.main.profile_graph.profile

import ru.antares.cheese_android.domain.errors.UIError

sealed interface ProfileUIError: UIError {
    data class LogoutError(override val message: String): ProfileUIError
    data class LoadProfileError(override val message: String) : ProfileUIError
}