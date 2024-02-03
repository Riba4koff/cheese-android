package ru.antares.cheese_android.presentation.view.authorization.confirm_code

import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.presentation.view.authorization.input_phone.ErrorState

@Immutable
data class ConfirmCodeState(
    val isLoading: Boolean = false,
    val codeIsWrong: Boolean = false,
    val code: String = "",
    val timer: Int = 5,
    val error: ErrorState = ErrorState(),
    val canMakeCallAgain: Boolean = false
)
