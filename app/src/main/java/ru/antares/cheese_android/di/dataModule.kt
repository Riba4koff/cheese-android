package ru.antares.cheese_android.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.local.datastore.token.TokenService
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.UserDataStore
import ru.antares.cheese_android.data.local.room.CheeseDataBase
import ru.antares.cheese_android.data.local.room.dao.catalog.CatalogDao

val dataModule: List<Module>
    get() = listOf(
        preferencesModule,
        dataBaseModule,
        daoModule,
        dataStoreModule
    )

private val preferencesModule = module {
    single { provideSettingsPreferences(androidApplication()) }
}

private val dataBaseModule = module {
    single { provideAppDataBase(get()) }
}

private val daoModule = module {
    single { provideCatalogDao(get()) }
}

private val dataStoreModule = module {
    singleOf(::TokenService) { bind<ITokenService>() }
    singleOf(::UserDataStore) { bind<IUserDataStore>() }
}

private const val PREFERENCES_FILE_KEY = "ru.antares.cheese_android"

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

private fun provideAppDataBase(context: Context): RoomDatabase {
    return Room.databaseBuilder(
        context = context,
        klass = CheeseDataBase::class.java,
        name = CheeseDataBase.DB_NAME
    ).fallbackToDestructiveMigration().build()
}

private fun provideCatalogDao(database: CheeseDataBase): CatalogDao {
    return database.catalogDao()
}