package ru.antares.cheese_android.domain.validators

import ru.antares.cheese_android.R

class EmailTextFieldValidator: TextFieldValidator {
    override fun validate(value: String): ValidationTextFieldResult {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

        return if (value.isEmpty()) ValidationTextFieldResult(
            text = R.string.the_field_must_not_be_empty, success = false
        ) else if (emailRegex.matches(value).not()) ValidationTextFieldResult(
            text = R.string.incorrect_email, success = false
        ) else ValidationTextFieldResult()
    }
}