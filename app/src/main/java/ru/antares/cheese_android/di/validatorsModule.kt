package ru.antares.cheese_android.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.antares.cheese_android.domain.validators.*

val validatorsModule = module {
    factoryOf(::PhoneTextFieldValidator) { bind<TextFieldValidator>() }
    factoryOf(::CredentialsTextFieldValidator) { bind<TextFieldValidator>() }
    factoryOf(::EmailTextFieldValidator) { bind<TextFieldValidator>() }
    factoryOf(::BirthdayTextFieldValidator) { bind<TextFieldValidator>() }
}