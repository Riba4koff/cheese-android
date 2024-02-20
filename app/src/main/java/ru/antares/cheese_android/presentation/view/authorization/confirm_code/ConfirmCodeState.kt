package ru.antares.cheese_android.presentation.view.authorization.confirm_code

import androidx.compose.runtime.Immutable

@Immutable
data class ConfirmCodeState(
    val isLoading: Boolean = false,
    val codeIsWrong: Boolean = false,
    val code: String = "",
    val timer: Int = 5,
    val error: ConfirmCodeUIError? = null,
    val canMakeCallAgain: Boolean = false
)
