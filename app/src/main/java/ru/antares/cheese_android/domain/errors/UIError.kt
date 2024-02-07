package ru.antares.cheese_android.domain.errors

interface UIError {
    val message: String
}

sealed interface ProfileUIError: UIError {
    data class LogoutError(override val message: String): ProfileUIError
    data class LoadProfileError(override val message: String) : ProfileUIError
}