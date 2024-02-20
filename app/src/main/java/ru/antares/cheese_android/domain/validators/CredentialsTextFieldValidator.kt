package ru.antares.cheese_android.domain.validators

import ru.antares.cheese_android.R

class CredentialsTextFieldValidator: TextFieldValidator {
    override fun validate(value: String): ValidationTextFieldResult {
        val digitsRegex = Regex("\\d")

        return if (value.isEmpty()) ValidationTextFieldResult(
            text = R.string.the_field_must_not_be_empty, success = false
        ) else if (digitsRegex.containsMatchIn(value)) ValidationTextFieldResult(
            text = R.string.the_field_must_not_contain_numbers, success = false
        ) else if (value.length < 2) ValidationTextFieldResult(
            text = R.string.length_must_be_more_than_1, success = false
        ) else ValidationTextFieldResult()
    }
}