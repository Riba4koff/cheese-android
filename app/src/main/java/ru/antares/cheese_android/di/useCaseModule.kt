package ru.antares.cheese_android.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.antares.cheese_android.domain.usecases.cart.GetCartFlowUseCase
import ru.antares.cheese_android.domain.usecases.cart.UpdateCartUseCase
import ru.antares.cheese_android.domain.usecases.personal_data.GetUserFromDSInfoUseCase

val useCaseModule: List<Module>
    get() = listOf(
        userUseCaseModule,
        cartUseCaseModule
    )

private val userUseCaseModule = module {
    factoryOf(::GetUserFromDSInfoUseCase)

}

private val cartUseCaseModule = module {
    factoryOf(::GetCartFlowUseCase)
    factoryOf(::UpdateCartUseCase)
}