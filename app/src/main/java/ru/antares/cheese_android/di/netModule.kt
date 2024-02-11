package ru.antares.cheese_android.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.local.datastore.token.ITokenService
import ru.antares.cheese_android.data.remote.services.addresses.AddressesService
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.main.profile.ProfileService
import ru.antares.cheese_android.data.remote.services.main.profile.response.Attachment
import ru.antares.cheese_android.data.remote.services.main.profile.response.AttachmentAdapter
import ru.antares.cheese_android.data.repository.auth.AuthorizationRepository
import ru.antares.cheese_android.data.repository.main.profile.ProfileRepository
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository
import ru.antares.cheese_android.domain.repository.IProfileRepository
import java.util.concurrent.TimeUnit

val netModule = module {
    factory { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }

    single { provideAuthorizationService(get()) }
    single { provideProfileService(get()) }
    single { provideAddressesService(get()) }

    singleOf(::AuthorizationRepository) { bind<IAuthorizationRepository>() }
    singleOf(::ProfileRepository) { bind<IProfileRepository>() }
}

private const val BASE_URL = "https://mobile-backend.cheese.asg-demo.ru/api/v1/"

fun provideOkHttpClient(tokenService: ITokenService): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

    return OkHttpClient.Builder()
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val token = runBlocking { tokenService.getToken().replace("\n", "") }
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Attachment::class.java, AttachmentAdapter())
        .create()
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
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