package ru.antares.cheese_android

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.*
import ru.antares.cheese_android.di.appModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(INFO)
            androidLogger(WARNING)
            androidLogger(ERROR)
            modules(appModule)
        }
    }
}