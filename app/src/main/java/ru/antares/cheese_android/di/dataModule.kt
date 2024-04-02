package ru.antares.cheese_android.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.local.datastore.token.TokenService
import ru.antares.cheese_android.data.local.datastore.user.IUserDataStore
import ru.antares.cheese_android.data.local.datastore.user.UserDataStore
import ru.antares.cheese_android.data.local.room.CheeseDataBase
import ru.antares.cheese_android.data.local.room.dao.cart.CartDao
import ru.antares.cheese_android.data.local.room.dao.cart.CartLocalStorage
import ru.antares.cheese_android.data.local.room.dao.cart.ICartLocalStorage
import ru.antares.cheese_android.data.local.room.dao.catalog.CategoriesDao
import ru.antares.cheese_android.data.local.room.dao.catalog.CategoriesLocalStorage
import ru.antares.cheese_android.data.local.room.dao.catalog.ICategoriesLocalStorage
import ru.antares.cheese_android.data.local.room.dao.products.IProductsLocalStorage
import ru.antares.cheese_android.data.local.room.dao.products.ProductsDao
import ru.antares.cheese_android.data.local.room.dao.products.ProductsLocalStorage

val localModule: List<Module>
    get() = listOf(
        localStorageModule,
        daoModule,
        dataBaseModule,
        preferencesModule,
        dataStoreModule
    )

private val dataBaseModule = module {
    singleOf(::provideCheeseAppDataBase)
}

private val preferencesModule = module {
    single { provideSettingsPreferences(androidApplication()) }
}

private val daoModule = module {
    singleOf(::provideCartDao)
    singleOf(::provideProductsDao)
    singleOf(::provideCategoryDao)
}

private val localStorageModule = module {
    factoryOf(::CartLocalStorage) { bind<ICartLocalStorage>() }
    factoryOf(::ProductsLocalStorage) { bind<IProductsLocalStorage>() }
    factoryOf(::CategoriesLocalStorage) { bind<ICategoriesLocalStorage>() }
}

private val dataStoreModule = module {
    singleOf(::TokenService) { bind<ITokenService>() }
    singleOf(::UserDataStore) { bind<IUserDataStore>() }
}

private const val PREFERENCES_FILE_KEY = "ru.antares.cheese_android"

private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)

private fun provideCheeseAppDataBase(context: Context) = Room.databaseBuilder(
    context = context,
    klass = CheeseDataBase::class.java, name = CheeseDataBase.DB_NAME
).fallbackToDestructiveMigration().build()

private fun provideCategoryDao(database: CheeseDataBase): CategoriesDao {
    return database.catalogDao()
}

private fun provideCartDao(database: CheeseDataBase): CartDao {
    return database.cartDao()
}

private fun provideProductsDao(database: CheeseDataBase): ProductsDao {
    return database.productsDao()
}

