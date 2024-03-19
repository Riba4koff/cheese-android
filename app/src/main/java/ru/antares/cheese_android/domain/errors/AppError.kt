package ru.antares.cheese_android.domain.errors

interface AppError {
    val message: String
}

sealed interface Error