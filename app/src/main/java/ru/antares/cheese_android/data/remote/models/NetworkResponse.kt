package ru.antares.cheese_android.data.remote.models

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T): NetworkResponse<T>()
    data class Error<T>(val message: String): NetworkResponse<T>()

    suspend fun onSuccess(block: suspend (data: T) -> Unit): NetworkResponse<T> {
        return if (this is Success) {
            block(this.data)
            this
        } else this
    }

    suspend fun onFailure(block: suspend (message: String) -> Unit): NetworkResponse<T> {
        return if (this is Error) {
            block(this.message)
            this
        } else this
    }
}
