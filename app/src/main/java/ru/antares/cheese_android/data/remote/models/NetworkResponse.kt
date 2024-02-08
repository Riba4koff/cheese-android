package ru.antares.cheese_android.data.remote.models

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T): NetworkResponse<T>()
    data class Error<T>(val message: String): NetworkResponse<T>()

    fun onSuccess(block: (data: T) -> Unit): NetworkResponse<T> {
        return if (this is Success) {
            block(this.data)
            this
        } else this
    }

    fun onFailure(block: (message: String) -> Unit): NetworkResponse<T> {
        return if (this is Error) {
            block(this.message)
            this
        } else this
    }
}
