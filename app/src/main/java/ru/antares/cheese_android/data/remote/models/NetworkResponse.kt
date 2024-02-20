package ru.antares.cheese_android.data.remote.models

data class NetworkError(
    val message: String,
    val code: Int? = null
)

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T): NetworkResponse<T>()
    data class Error<T>(val error: NetworkError): NetworkResponse<T>()

    suspend fun onSuccess(block: suspend (data: T) -> Unit): NetworkResponse<T> {
        return if (this is Success) {
            block(this.data)
            this
        } else this
    }

    suspend fun onFailure(block: suspend (error: NetworkError) -> Unit): NetworkResponse<T> {
        return if (this is Error) {
            block(this.error)
            this
        } else this
    }

    fun getOrNull(): T? {
        return if (this is Success) this.data
        else null
    }
}
