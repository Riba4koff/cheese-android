package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.validators.ValidationTextFieldResult





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
        val error: PersonalDataUIError,
        override val key: PersonalDataViewStateKey = PersonalDataViewStateKey.ERROR
    ) : PersonalDataViewState
}