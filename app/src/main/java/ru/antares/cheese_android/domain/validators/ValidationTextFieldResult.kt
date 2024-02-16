package ru.antares.cheese_android.domain.validators

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.R

@Immutable
data class ValidationTextFieldResult(
    @StringRes val text: Int? = null,
    val success: Boolean = true
)
