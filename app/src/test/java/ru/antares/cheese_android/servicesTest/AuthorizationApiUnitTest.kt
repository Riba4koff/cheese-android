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
import ru.antares.cheese_android.data.remote.api.auth.AuthorizationApi
import ru.antares.cheese_android.data.remote.api.auth.dto.DeviceDTO
import ru.antares.cheese_android.data.remote.api.auth.request.SendCodeRequest
import ru.antares.cheese_android.data.remote.api.main.profile.response.Attachment
import ru.antares.cheese_android.data.remote.api.main.profile.response.AttachmentAdapter
import ru.antares.cheese_android.data.repository.auth.models.DeviceModel
import ru.antares.cheese_android.data.repository.auth.models.SessionModel
import ru.antares.cheese_android.data.repository.auth.responses.SendCodeResponse

//TODO Добавить тестовый контекст для TokenService

/**
 * This class tests the AuthorizationService interface,
 * which is used to work with user authorization
 * */
class AuthorizationApiUnitTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var authService: AuthorizationApi

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
        val response = authService.call(phone)

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
        val response = authService.code(phone, sendCodeRequest)

        assertEquals(expectedResponse, response)
    }

    private fun createTestAuthService(): AuthorizationApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(Attachment::class.java, AttachmentAdapter())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://mobile-backend.cheese.asg-demo.ru/api/v1/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(AuthorizationApi::class.java)
    }
}