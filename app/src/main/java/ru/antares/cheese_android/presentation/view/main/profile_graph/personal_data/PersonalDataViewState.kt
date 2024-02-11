package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.domain.errors.UIError
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

enum class PersonalDataViewStateKey {
    LOADING,
    SUCCESS,
    ERROR
}

sealed interface PersonalDataViewState {
    val key: PersonalDataViewStateKey

    data class Loading(
        override val key: PersonalDataViewStateKey = PersonalDataViewStateKey.LOADING
    ) : PersonalDataViewState

    data class Success(
        val surname: String = "",
        val name: String = "",
        val patronymic: String = "",
        val birthday: String = "",
        val email: String = "",
        val phone: String = "",
        val verifiedPhone: Boolean = false,
        val verifiedEmail: Boolean = false,
        val validation: TextFieldValidations = TextFieldValidations(),
        override val key: PersonalDataViewStateKey = PersonalDataViewStateKey.SUCCESS,
    ) : PersonalDataViewState

    data class Error(
        val error: UIError,
        override val key: PersonalDataViewStateKey = PersonalDataViewStateKey.ERROR
    ) : PersonalDataViewState
}