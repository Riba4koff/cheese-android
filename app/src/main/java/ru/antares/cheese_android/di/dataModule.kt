package ru.antares.cheese_android.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.antares.cheese_android.data.local.datastore.SecurityTokenService
import ru.antares.cheese_android.data.local.datastore.TokenService

val dataModule = module {
    single { provideSettingsPreferences(androidApplication()) }
    singleOf(::TokenService) { bind<SecurityTokenService>() }
}

private const val PREFERENCES_FILE_KEY = "ru.antares.cheese_android"

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)