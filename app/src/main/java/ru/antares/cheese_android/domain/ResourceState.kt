package ru.antares.cheese_android.domain

import ru.antares.cheese_android.domain.errors.UIError

sealed class ResourceState<T> {
    data class Loading<T>(val isLoading: Boolean): ResourceState<T>()
    data class Error<T>(val error: UIError): ResourceState<T>()
    data class Success<T>(val data: T): ResourceState<T>()

    suspend fun onLoading(block: suspend (isLoading: Boolean) -> Unit): ResourceState<T> {
        return if (this is Loading) {
            block(this.isLoading)
            this
        } else this
    }

    suspend fun onError(block: suspend (error: UIError) -> Unit): ResourceState<T> {
        return if (this is Error) {
            block(this.error)
            this
        } else this
    }

    suspend fun onSuccess(block: suspend (data: T) -> Unit): ResourceState<T> {
        return if (this is Success) {
            block(this.data)
            this
        } else this
    }
}