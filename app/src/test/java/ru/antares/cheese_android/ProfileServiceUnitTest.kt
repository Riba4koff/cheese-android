package ru.antares.cheese_android

import com.google.gson.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.*
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.remote.Response
import ru.antares.cheese_android.data.remote.services.profile.ProfileService
import ru.antares.cheese_android.data.remote.services.profile.request.UpdateProfileRequest
import ru.antares.cheese_android.data.remote.services.profile.response.Attachment
import ru.antares.cheese_android.data.remote.services.profile.response.AttachmentAdapter
import ru.antares.cheese_android.data.remote.services.profile.response.ProfileResponse
import java.util.UUID


/**
 *  Class for testing the ProfileService interface
 * */
class ProfileServiceUnitTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var profileService: ProfileService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        profileService = createTestProfileService()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testUpdateProfile() {
        val updatingSurname = "Иванов"
        val updatingName = "Иван"
        val updatingPatronymic = "Иванович"

        val expectedResponse = Response(
            ProfileResponse(
                "cb9a5f9b-b502-4fa2-bc1b-abc939e5359b",
                updatingSurname,
                updatingName,
                updatingPatronymic,
                "2003-12-20",
                listOf(
                    Attachment.Phone(verified = false, phone =  "+79021486080"),
                    Attachment.Email(verified = false, email = "qwq4444@asdasd.ru")
                )
            )
        )
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(expectedResponse)))

        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBdWRpZW5jZSIsImlzcyI6IkNoZWVzZU1vYmlsZSIsIlNFU1NJT05fSUQiOiJlMDM2NDM2MS1lM2ZlLTQzNjItOGZjMi1iODk1Zjc0MWJiYjkifQ.r9jpTlSFEo5IVlBiEnrrpRk4eCQAJ5chgfQGsHlMSl8"

        val request = UpdateProfileRequest(
            surname = updatingSurname,
            firstname = updatingName,
            patronymic = updatingPatronymic
        )
        val response = runBlocking { profileService.update(bearerToken = "Bearer $token", request = request) }

        assertEquals(expectedResponse, response)
    }

    /**
     * Enter the data that is on the server, otherwise the test will not pass!
     * */
    @Test
    fun testGetProfile() {
        val expectedResponse = Response(
            ProfileResponse(
                "cb9a5f9b-b502-4fa2-bc1b-abc939e5359b",
                "Рыбаков",
                "Павел",
                "Алексеевич",
                "2003-12-20",
                listOf(
                    Attachment.Phone(verified = false, phone =  "+79021486080"),
                    Attachment.Email(verified = false, email = "qwq4444@asdasd.ru")
                )
            )
        )
        mockWebServer.enqueue(MockResponse().setBody(Gson().toJson(expectedResponse)))

        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBdWRpZW5jZSIsImlzcyI6IkNoZWVzZU1vYmlsZSIsIlNFU1NJT05fSUQiOiJlMDM2NDM2MS1lM2ZlLTQzNjItOGZjMi1iODk1Zjc0MWJiYjkifQ.r9jpTlSFEo5IVlBiEnrrpRk4eCQAJ5chgfQGsHlMSl8"
        val response = runBlocking { profileService.get("Bearer $token") }

        assertEquals(expectedResponse, response)
    }

    private fun createTestProfileService(): ProfileService {
        val gson = GsonBuilder()
            .registerTypeAdapter(Attachment::class.java, AttachmentAdapter())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://mobile-backend.cheese.asg-demo.ru/api/v1//"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ProfileService::class.java)
    }
}