package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.validators.ValidationTextFieldResult

/**
 * @author pavelrybakov
 * Created 17.05.2024 at 17:59
 * Android Studio
 */

@optics
@Immutable
data class CreateAddressScreenState(
    val loading: Boolean = false,
    val city: String = "",
    val street: String = "",
    val house: String = "",
    val building: String = "",
    val entrance: String = "",
    val apartment: String = "",
    val floor: String = "",
    val comment: String = "",
    val validation: CreateAddressScreenValidation = CreateAddressScreenValidation()
) { companion object }

@optics
@Immutable
data class CreateAddressScreenValidation(
    val cityValidation: ValidationTextFieldResult = ValidationTextFieldResult(),
    val streetValidation: ValidationTextFieldResult = ValidationTextFieldResult(),
    val houseValidation: ValidationTextFieldResult = ValidationTextFieldResult(),
    val commentValidation: ValidationTextFieldResult = ValidationTextFieldResult()
) {
    companion object

    fun allFieldsAreValid(): Boolean {
        return cityValidation.success
                && streetValidation.success
                && houseValidation.success
                && commentValidation.success
    }
}