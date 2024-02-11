package ru.antares.cheese_android.servicesTest

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.remote.models.NetworkResponse
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogService
import ru.antares.cheese_android.data.repository.util.safeNetworkCall

class CatalogServiceUnitTest {

    private lateinit var service: CatalogService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        service = createCatalogService()
    }

    @After
    fun close() {
        mockWebServer.shutdown()
    }

    @Test
    fun getCategories() {
        val response = runBlocking { safeNetworkCall { service.get() } }

        when (response) {
            is NetworkResponse.Error -> {
                assertNotNull(response.message)
            }
            is NetworkResponse.Success -> {
                assertTrue(response.data.result.isNotEmpty())
            }
        }
    }

    private fun createCatalogService(): CatalogService {
        return createRetrofit().create(CatalogService::class.java)
    }

    private fun createRetrofit(): Retrofit {
        val gson = createGson()
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://mobile-backend.cheese.asg-demo.ru/api/v1/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun createGson(): Gson {
        return GsonBuilder().create()
    }
}