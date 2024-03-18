package ru.antares.cheese_android.presentation.view.authorization.input_phone

import androidx.compose.runtime.Immutable

@Immutable
data class InputPhoneState(
    val phone: String = "",
    val isLoading: Boolean = false,
    val error: InputPhoneAppError? = null,
    val phoneIsValid: Boolean = true
)
