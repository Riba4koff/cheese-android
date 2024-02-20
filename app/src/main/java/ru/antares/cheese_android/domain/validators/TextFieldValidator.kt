package ru.antares.cheese_android.domain.validators

interface TextFieldValidator {
    fun validate(value: String): ValidationTextFieldResult
}