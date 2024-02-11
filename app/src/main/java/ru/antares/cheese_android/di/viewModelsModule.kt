package ru.antares.cheese_android.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeViewModel
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneViewModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CatalogViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileViewModel
import ru.antares.cheese_android.presentation.view.splash.SplashScreenViewModel

val viewModelsModule: List<Module>
    get() = listOf(
        splashScreenViewModelModule,
        authViewModelsModule,
        profileViewModelsModule,
        catalogViewModelsModule
    )

private val splashScreenViewModelModule = module {
    viewModelOf(::SplashScreenViewModel)
}

private val authViewModelsModule = module {
    viewModelOf(::InputPhoneViewModel)
    viewModelOf(::ConfirmCodeViewModel)
}

private val profileViewModelsModule = module {
    viewModelOf(::ProfileViewModel)
    viewModelOf(::PersonalDataViewModel)
}

private val catalogViewModelsModule = module {
    viewModelOf(::CatalogViewModel)
}