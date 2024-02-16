package ru.antares.cheese_android.servicesTest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.remote.services.auth.AuthorizationService
import ru.antares.cheese_android.data.remote.services.auth.dto.DeviceDTO
import ru.antares.cheese_android.data.remote.services.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.services.main.profile.response.Attachment
import ru.antares.cheese_android.data.remote.services.main.profile.response.AttachmentAdapter
import ru.antares.cheese_android.data.repository.auth.models.DeviceModel
import ru.antares.cheese_android.data.repository.auth.models.SessionModel
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse

//TODO Добавить тестовый контекст для TokenService

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
    fun testMakeCallTest() = runBlocking {
        val phone = "+79116132860"
        val response = authService.makeCall(phone)

        assertNotNull(response)
    }

    @Test
    fun testSendCodeTest() = runBlocking {
        val expectedResponse = SendCodeResponse(
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBdWRpZW5jZSIsImlzcyI6IkNoZWVzZU1vYmlsZSIsIlNFU1NJT05fSUQiOiI0NjJlOWZiYS1iOTJhLTQ4NjktYmIwMy1iOTZjNmNlN2Q2NzcifQ.xelhbKS7HsshMyw9S8Sz1UerPv47xiNHcyqzTN4eb0k",
            sessionModel = SessionModel(
                authorizationType = "",
                authorizedObject = "",
                device = DeviceModel(
                    firebaseToken = "",
                    firmware = "",
                    id = null,
                    version = ""
                ),
                deviceId = "",
                start = "",
                finish = "",
                id = "",
                opened = false
            )
        )
        mockWebServer.enqueue(
            MockResponse().setBody(
                Gson().toJson(expectedResponse)
            )
        )

        val phone = "+79116132860"
        val sendCodeRequest =
            SendCodeRequest(
                code = "9915".toInt(), device = DeviceDTO(
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
        val gson = GsonBuilder()
            .registerTypeAdapter(Attachment::class.java, AttachmentAdapter())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://mobile-backend.cheese.asg-demo.ru/api/v1/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(AuthorizationService::class.java)
    }
}