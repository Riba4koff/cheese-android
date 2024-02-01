package ru.antares.cheese_android

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.auth.dto.DeviceDTO
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.services.auth.response.MakeCallResponse
import ru.antares.cheese_android.data.remote.services.auth.response.SendCodeResponse

/**
 * This class tests the AuthorizationService interface,
 * which is used to work with user authorization
 * */
class AuthorizationServiceUnitTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var authService: AuthorizationService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        authService = createTestAuthService()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testMakeCall() = runBlocking {
        val expectedResponse = MakeCallResponse(false)
        mockWebServer.enqueue(
            MockResponse().setBody(
                Gson().toJson(expectedResponse)
            )
        )

        val phone = "+79425223123"
        val response = authService.makeCall(phone)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun testSendCode() = runBlocking {
        val expectedResponse = SendCodeResponse(true)
        mockWebServer.enqueue(
            MockResponse().setBody(
                Gson().toJson(expectedResponse)
            )
        )

        val phone = "+79425223123"
        val sendCodeRequest =
            SendCodeRequest(
                code = "0000".toInt(), device = DeviceDTO(
                    firebaseToken = "",
                    firmware = "",
                    id = null,
                    version = ""
                )
            )
        val response = authService.sendCode(phone, sendCodeRequest)

        assertEquals(expectedResponse, response)
    }

    private fun createTestAuthService(): AuthorizationService {
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://mobile-backend.cheese.asg-demo.ru/api/v1/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(AuthorizationService::class.java)
    }
}