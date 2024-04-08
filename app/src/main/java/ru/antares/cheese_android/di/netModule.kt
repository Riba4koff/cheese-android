package ru.antares.cheese_android.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.local.datastore.token.IAuthorizationDataStore
import ru.antares.cheese_android.data.remote.services.addresses.AddressesService
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationServiceHandler
import ru.antares.cheese_android.data.remote.services.cart.CartService
import ru.antares.cheese_android.data.remote.services.cart.CartServiceHandler
import ru.antares.cheese_android.data.remote.services.community.CommunityService
import ru.antares.cheese_android.data.remote.services.community.CommunityServiceHandler
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogService
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogServiceHandler
import ru.antares.cheese_android.data.remote.services.main.products.ProductsService
import ru.antares.cheese_android.data.remote.services.main.products.ProductsServiceHandler
import ru.antares.cheese_android.data.remote.services.main.profile.ProfileService
import ru.antares.cheese_android.data.remote.services.main.profile.ProfileServiceHandler
import ru.antares.cheese_android.data.remote.services.main.profile.response.Attachment
import ru.antares.cheese_android.data.remote.services.main.profile.response.AttachmentAdapter
import ru.antares.cheese_android.data.repository.auth.AuthorizationRepository
import ru.antares.cheese_android.data.repository.main.CartRepository
import ru.antares.cheese_android.data.repository.main.CommunityRepository
import ru.antares.cheese_android.data.repository.main.ProductsRepository
import ru.antares.cheese_android.data.repository.main.ProfileRepository
import ru.antares.cheese_android.data.repository.main.catalog.CatalogRepository
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository
import ru.antares.cheese_android.domain.repository.ICartRepository
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.domain.repository.ICommunityRepository
import ru.antares.cheese_android.domain.repository.IProductsRepository
import ru.antares.cheese_android.domain.repository.IProfileRepository
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://mobile-backend.cheese.asg-demo.ru/api/v1/"

val netModule: List<Module>
    get() = listOf(
        retrofitModule,
        okHttpModule,
        repositoryModule,
        servicesModule,
        serviceHandlerModule
    )

private val retrofitModule = module {
    single { provideRetrofit(get()) }
}

private val okHttpModule = module {
    factory { provideOkHttpClient(get()) }
}

private val repositoryModule = module {
    singleOf(::AuthorizationRepository) { bind<IAuthorizationRepository>() }
    singleOf(::ProfileRepository) { bind<IProfileRepository>() }
    singleOf(::CatalogRepository) { bind<ICatalogRepository>() }
    singleOf(::ProductsRepository) { bind<IProductsRepository>() }
    singleOf(::CartRepository) { bind<ICartRepository>() }
    singleOf(::CommunityRepository) { bind<ICommunityRepository>() }
}

private val servicesModule = module {
    single { provideAuthorizationService(get()) }
    single { provideProfileService(get()) }
    single { provideAddressesService(get()) }
    single { provideCatalogService(get()) }
    single { provideProductsService(get()) }
    single { provideCartService(get()) }
    single { provideCommunityService(get()) }
}

private val serviceHandlerModule = module {
    factoryOf(::CommunityServiceHandler)
    factoryOf(::CartServiceHandler)
    factoryOf(::ProfileServiceHandler)
    factoryOf(::CatalogServiceHandler)
    factoryOf(::ProductsServiceHandler)
    factoryOf(::AuthorizationServiceHandler)
}

private fun provideAuthorizationService(retrofit: Retrofit): AuthorizationService =
    retrofit.create(AuthorizationService::class.java)

private fun provideProfileService(retrofit: Retrofit): ProfileService =
    retrofit.create(ProfileService::class.java)

private fun provideAddressesService(retrofit: Retrofit): AddressesService =
    retrofit.create(AddressesService::class.java)

private fun provideCatalogService(retrofit: Retrofit): CatalogService =
    retrofit.create(CatalogService::class.java)

private fun provideProductsService(retrofit: Retrofit): ProductsService =
    retrofit.create(ProductsService::class.java)

private fun provideCommunityService(retrofit: Retrofit): CommunityService =
    retrofit.create(CommunityService::class.java)

private fun provideCartService(retrofit: Retrofit): CartService =
    retrofit.create(CartService::class.java)

private fun provideOkHttpClient(tokenService: IAuthorizationDataStore): OkHttpClient {
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

private fun provideRetrofit(client: OkHttpClient): Retrofit {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(Attachment::class.java, AttachmentAdapter())
        .create()
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}