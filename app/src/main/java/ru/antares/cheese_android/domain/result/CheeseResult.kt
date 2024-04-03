package ru.antares.cheese_android.domain.result

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 14:33
 * Android Studio
 */

sealed interface CheeseResult<out E : CheeseError, out D> {
    data class Loading<E : CheeseError, D>(val isLoading: Boolean) : CheeseResult<E, D>
    data class Success<out E : CheeseError, out D>(val data: D) : CheeseResult<E, D>
    data class Error<out E : CheeseError, out D>(val error: E) : CheeseResult<E, D>

    fun onLoading(block: (loading: Boolean) -> Unit): CheeseResult<E, D> = if (this is Loading) {
        block(this.isLoading)
        this
    } else this

    suspend fun onSuccess(block: suspend (data: D) -> Unit): CheeseResult<E, D> = if (this is Success) {
        block(this.data)
        this
    } else this

    suspend fun onError(block: suspend (error: E) -> Unit): CheeseResult<E, D> = if (this is Error) {
        block(this.error)
        this
    } else this
}