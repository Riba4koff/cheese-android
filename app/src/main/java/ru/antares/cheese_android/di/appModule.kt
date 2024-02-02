@file:OptIn(ExperimentalSerializationApi::class)

package ru.antares.cheese_android.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.antares.cheese_android.data.remote.services.addresses.AddressesService
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.profile.ProfileService
import ru.antares.cheese_android.presentation.view.splash.SplashScreenViewModel


val appModule = listOf(
    netModule,
    dataModule,
    viewModelsModule
)

