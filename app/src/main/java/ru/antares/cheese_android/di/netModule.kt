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
import ru.antares.cheese_android.data.remote.api.main.addresses.AddressesApi
import ru.antares.cheese_android.data.remote.api.main.addresses.AddressesApiHandler
import ru.antares.cheese_android.data.remote.api.auth.AuthorizationApi
import ru.antares.cheese_android.data.remote.api.auth.AuthorizationApiHandler
import ru.antares.cheese_android.data.remote.api.main.cart.CartApi
import ru.antares.cheese_android.data.remote.api.main.cart.CartApiHandler
import ru.antares.cheese_android.data.remote.api.main.community.CommunityApi
import ru.antares.cheese_android.data.remote.api.main.community.CommunityApiHandler
import ru.antares.cheese_android.data.remote.api.main.catalog.CatalogApi
import ru.antares.cheese_android.data.remote.api.main.catalog.CatalogApiHandler
import ru.antares.cheese_android.data.remote.api.main.products.ProductsApi
import ru.antares.cheese_android.data.remote.api.main.products.ProductsApiHandler
import ru.antares.cheese_android.data.remote.api.main.profile.ProfileApi
import ru.antares.cheese_android.data.remote.api.main.profile.ProfileApiHandler
import ru.antares.cheese_android.data.remote.api.main.profile.response.Attachment
import ru.antares.cheese_android.data.remote.api.main.profile.response.AttachmentAdapter
import ru.antares.cheese_android.data.repository.auth.AuthorizationRepository
import ru.antares.cheese_android.data.repository.main.AddressesRepository
import ru.antares.cheese_android.data.repository.main.CartRepository
import ru.antares.cheese_android.data.repository.main.CommunityRepository
import ru.antares.cheese_android.data.repository.main.OrdersRepository
import ru.antares.cheese_android.data.repository.main.ProductsRepository
import ru.antares.cheese_android.data.repository.main.ProfileRepository
import ru.antares.cheese_android.data.repository.main.catalog.CatalogRepository
import ru.antares.cheese_android.domain.repository.IAddressesRepository
import ru.antares.cheese_android.domain.repository.IAuthorizationRepository
import ru.antares.cheese_android.domain.repository.ICartRepository
import ru.antares.cheese_android.domain.repository.ICatalogRepository
import ru.antares.cheese_android.domain.repository.ICommunityRepository
import ru.antares.cheese_android.domain.repository.IOrdersRepository
import ru.antares.cheese_android.domain.repository.IProductsRepository
import ru.antares.cheese_android.domain.repository.IProfileRepository
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://mobile-backend.cheese.asg-demo.ru/api/v1/"

val netModule: List<Module>
    get() = listOf(
        retrofitModule,
        okHttpModule,
        repositoryModule,
        apiModule,
        apiHandlerModule
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
    singleOf(::AddressesRepository) { bind<IAddressesRepository>() }
    singleOf(::OrdersRepository) { bind<IOrdersRepository>() }
}

private val apiModule = module {
    single { provideAuthorizationApi(get()) }
    single { provideProfileApi(get()) }
    single { provideAddressesApi(get()) }
    single { provideCatalogApi(get()) }
    single { provideProductsApi(get()) }
    single { provideCartApi(get()) }
    single { provideCommunityApi(get()) }
}

private val apiHandlerModule = module {
    factoryOf(::CommunityApiHandler)
    factoryOf(::CartApiHandler)
    factoryOf(::ProfileApiHandler)
    factoryOf(::CatalogApiHandler)
    factoryOf(::ProductsApiHandler)
    factoryOf(::AuthorizationApiHandler)
    factoryOf(::AddressesApiHandler)
}

private fun provideAuthorizationApi(retrofit: Retrofit): AuthorizationApi =
    retrofit.create(AuthorizationApi::class.java)

private fun provideProfileApi(retrofit: Retrofit): ProfileApi =
    retrofit.create(ProfileApi::class.java)

private fun provideAddressesApi(retrofit: Retrofit): AddressesApi =
    retrofit.create(AddressesApi::class.java)

private fun provideCatalogApi(retrofit: Retrofit): CatalogApi =
    retrofit.create(CatalogApi::class.java)

private fun provideProductsApi(retrofit: Retrofit): ProductsApi =
    retrofit.create(ProductsApi::class.java)

private fun provideCommunityApi(retrofit: Retrofit): CommunityApi =
    retrofit.create(CommunityApi::class.java)

private fun provideCartApi(retrofit: Retrofit): CartApi =
    retrofit.create(CartApi::class.java)

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