package ru.antares.cheese_android.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.antares.cheese_android.domain.usecases.personal_data.GetUserFromDSInfoUseCase

val useCaseModule = module {
    factoryOf(::GetUserFromDSInfoUseCase)
}