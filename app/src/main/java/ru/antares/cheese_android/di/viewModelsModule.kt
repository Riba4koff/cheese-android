package ru.antares.cheese_android.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.ConfirmCodeViewModel
import ru.antares.cheese_android.presentation.view.authorization.input_phone.InputPhoneViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataViewModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.profile.ProfileViewModel
import ru.antares.cheese_android.presentation.view.splash.SplashScreenViewModel

val viewModelsModule = module {
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::InputPhoneViewModel)
    viewModelOf(::ConfirmCodeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::PersonalDataViewModel)
}