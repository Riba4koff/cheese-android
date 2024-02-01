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
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.profile.ProfileService

private const val BASE_URL = "https://mobile-backend.cheese.asg-demo.ru/api/v1/"

val appModule = module {
    factory { provideRetrofit() }

    single { provideAuthorizationService(get()) }
    single { provideProfileService(get()) }
}

fun provideRetrofit(): Retrofit {
    val gson: Gson = GsonBuilder().create()

    return Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(OkHttpClient.Builder().build())
        .build()
}

fun provideAuthorizationService(retrofit: Retrofit): AuthorizationService {
    return retrofit.create(AuthorizationService::class.java)
}

fun provideProfileService(retrofit: Retrofit): ProfileService {
    return retrofit.create(ProfileService::class.java)
}