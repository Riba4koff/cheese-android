package ru.antares.cheese_android.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.local.datastore.token.TokenService
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.UserDataStore

val dataModule = module {
    single { provideSettingsPreferences(androidApplication()) }
    singleOf(::TokenService) { bind<ITokenService>() }
    singleOf(::UserDataStore) { bind<IUserDataStore>() }
}

private const val PREFERENCES_FILE_KEY = "ru.antares.cheese_android"

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)