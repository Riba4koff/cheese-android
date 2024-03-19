package ru.antares.cheese_android.domain

/**
 * @author pavelrybakov
 * Created 19.03.2024 at 14:27
 * Android Studio
 */
interface Error

typealias RootError = Error

sealed interface Result<out E : RootError, out D> {
    data class Success<out E : RootError, out D>(val data: D) : Result<E, D>
    data class Loading<out E : RootError, out D>(val loading: Boolean) : Result<E, D>
    data class Error<out E : RootError, out D>(val error: E) : Result<E, D>
}

inline fun <E : RootError, D> Result<E, D>.onError(block: (error: E) -> Unit): Result<E, D> {
    return if (this is Result.Error<E, D>) {
        block(error)
        this
    } else this
}

inline fun <E : RootError, D> Result<E, D>.onLoading(block: (isLoading: Boolean) -> Unit): Result<E, D> {
    return if (this is Result.Loading<E, D>) {
        block(loading)
        this
    } else this
}

inline fun <E : RootError, D> Result<E, D>.onSuccess(block: (data: D) -> Unit): Result<E, D> {
    return if (this is Result.Success<E, D>) {
        block(this.data)
        this
    } else this
}