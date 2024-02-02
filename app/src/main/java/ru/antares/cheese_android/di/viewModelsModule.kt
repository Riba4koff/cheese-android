package ru.antares.cheese_android.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.antares.cheese_android.presentation.view.splash.SplashScreenViewModel

val viewModelsModule = module {
    viewModelOf(::SplashScreenViewModel)
}