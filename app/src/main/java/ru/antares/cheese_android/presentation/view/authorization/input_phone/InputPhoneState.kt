package ru.antares.cheese_android.presentation.view.authorization.input_phone

import androidx.compose.runtime.Immutable

@Immutable
data class ErrorState(
    val isError: Boolean = false,
    val message: String = ""
)

@Immutable
data class InputPhoneState(
    val phone: String = "",
    val isLoading: Boolean = false,
    val error: ErrorState = ErrorState(),
    val phoneIsValid: Boolean = true
)

/*
sealed interface InputPhoneState {
    data object Loading: InputPhoneState

    @Immutable
    data class Success(
        val phone: String = ""
    ): InputPhoneState

    data class Error(val message: String): InputPhoneState

    fun InputPhoneState.handle(
        onLoading: () -> InputPhoneState = { this },
        onSuccess: (Success) -> InputPhoneState = { it },
        onError: (message: String) -> InputPhoneState = { this }
    ): InputPhoneState {
        return when (this) {
            is Loading -> onLoading()
            is Success -> onSuccess(this)
            is Error -> onError(this.message)
        }
    }
    @Composable
    fun InputPhoneState.onSuccess(block: @Composable (Success) -> Unit): InputPhoneState {
        return if (this is Success) {
            block(this)
            this
        } else this
    }

    @Composable
    fun InputPhoneState.onLoading(block: @Composable () -> Unit): InputPhoneState {
        return if (this is Loading) {
            block()
            this
        }
        else this
    }

    @Composable
    fun InputPhoneState.onError(block: @Composable (message: String) -> Unit): InputPhoneState {
        return if (this is Error) {
            block(this.message)
            this
        }
        else this
    }
}*/