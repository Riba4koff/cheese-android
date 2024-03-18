package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.errors.AppError


@optics
@Immutable
data class PersonalDataState(
    val uiState: PersonalDataLoadingState = PersonalDataLoadingState.LOADING,
    val error: AppError? = null,
    val surname: String = "",
    val name: String = "",
    val patronymic: String = "",
    val birthday: String = "",
    val email: String = "",
    val phone: String = "",
    val enabledBirthday: Boolean = true,
    val verifiedPhone: Boolean = false,
    val verifiedEmail: Boolean = false,
    val validation: TextFieldValidations = TextFieldValidations()
) { companion object }