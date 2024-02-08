package ru.antares.cheese_android.domain.validators

import ru.antares.cheese_android.R

class PhoneTextFieldValidator: TextFieldValidator {
    override fun validate(value: String): ValidationTextFieldResult {
        val phoneRegex = Regex("\\d{10}")

        return if (value.isEmpty()) ValidationTextFieldResult(
            text = R.string.the_field_must_not_be_empty, success = false
        ) else if (phoneRegex.matches(value).not()) ValidationTextFieldResult(
            text = R.string.incorrect_phone_number, success = false
        ) else ValidationTextFieldResult()
    }
}