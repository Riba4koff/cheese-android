package ru.antares.cheese_android.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.remote.services.addresses.AddressesService
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.profile.ProfileService

val netModule = module {
    factory { provideRetrofit() }

    single { provideAuthorizationService(get()) }
    single { provideProfileService(get()) }
    single { provideAddressesService(get()) }
}

private const val BASE_URL = "https://mobile-backend.cheese.asg-demo.ru/api/v1/"

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

fun provideAddressesService(retrofit: Retrofit): AddressesService {
    return retrofit.create(AddressesService::class.java)
}