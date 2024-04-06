package ru.antares.cheese_android.domain

import ru.antares.cheese_android.domain.errors.CheeseError

/**
 * Result.kt
 * @author Павел
 * Created by on 02.04.2024 at 19:23
 * Android studio
 */

sealed interface Result<out E : CheeseError, out D> {
    data class Success<out E : CheeseError, out D>(val data: D) : Result<E, D>
    data class Error<out E: CheeseError, out D>(val error: E): Result<E, D>

    fun onSuccess(block: (data: D) -> Unit): Result<E, D> {
        return if (this is Success) {
            block(data)
            this
        } else this
    }

    fun onError(block: (error: E) -> Unit): Result<E, D> {
        return if (this is Error) {
            block(error)
            this
        } else this
    }

    fun getOrNull(): D? {
        return if (this is Success) {
            data
        } else null
    }
}