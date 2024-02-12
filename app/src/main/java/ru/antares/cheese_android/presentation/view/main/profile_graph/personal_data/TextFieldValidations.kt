package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.domain.validators.ValidationTextFieldResult

@Immutable
data class TextFieldValidations(
    val nameValidationResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    val surnameValidationResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    val patronymicValidationResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    val phoneValidationResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    val emailValidationResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    val birthdayValidationResult: ValidationTextFieldResult = ValidationTextFieldResult(),
) {
    val allFieldsAreValid: Boolean =
        surnameValidationResult.success && nameValidationResult.success &&
                patronymicValidationResult.success && phoneValidationResult.success &&
                emailValidationResult.success && birthdayValidationResult.success
}