package ru.antares.cheese_android.data.local.models

sealed interface LocalResponse<DATA> {
    data class Success<DATA>(val data: DATA): LocalResponse<DATA>
    data class Error<DATA>(val message: String): LocalResponse<DATA>

    fun onSuccess(block: (data: DATA) -> Unit): LocalResponse<DATA> {
        return if (this is Success) {
            block(this.data)
            this
        } else this
    }

    fun onFailure(block: (message: String) -> Unit): LocalResponse<DATA> {
        return if (this is Error) {
            block(this.message)
            this
        } else this
    }
}