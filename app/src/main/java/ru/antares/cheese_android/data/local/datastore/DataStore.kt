package ru.antares.cheese_android.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore

val Context.appDataStore by preferencesDataStore("AppDataStore", produceMigrations = { context ->
    listOf(SharedPreferencesMigration(context, "secure_data"))
})