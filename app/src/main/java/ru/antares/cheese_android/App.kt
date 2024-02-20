package ru.antares.cheese_android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.ERROR
import org.koin.core.logger.Level.INFO
import org.koin.core.logger.Level.WARNING
import ru.antares.cheese_android.di.dataModule
import ru.antares.cheese_android.di.netModule
import ru.antares.cheese_android.di.useCaseModule
import ru.antares.cheese_android.di.validatorsModule
import ru.antares.cheese_android.di.viewModelsModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(INFO)
            androidLogger(WARNING)
            androidLogger(ERROR)
            modules(netModule)
            modules(dataModule)
            modules(viewModelsModule)
            modules(validatorsModule, useCaseModule)
        }
    }
}