package ru.antares.cheese_android.servicesTest

import com.google.gson.GsonBuilder
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNotSame
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.antares.cheese_android.data.remote.services.addresses.AddressesService
import ru.antares.cheese_android.data.remote.services.addresses.dto.AddressDTO
import ru.antares.cheese_android.data.remote.services.addresses.request.CreateAddressRequest
import ru.antares.cheese_android.data.remote.services.addresses.request.UpdateAddressRequest

/**
 * Class for testing the functions of the AddressesService interface.
 * */
class AddressesServiceUnitTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var addressesService: AddressesService

    private companion object {
        const val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJBdWRpZW5jZSIsImlzcyI6IkNoZWVzZU1vYmlsZSIsIlNFU1NJT05fSUQiOiJlMDM2NDM2MS1lM2ZlLTQzNjItOGZjMi1iODk1Zjc0MWJiYjkifQ.r9jpTlSFEo5IVlBiEnrrpRk4eCQAJ5chgfQGsHlMSl8"

        val cities = listOf(
            "Москва",
            "Санкт-Петербург",
            "Великий Новгороод",
            "Казань",
            "Хабаровск"
        )
        val streets = listOf(
            "Романовская",
            "Прихожая",
            "Ленина",
            "Псковская",
            "Нехинская",
            "Большая Санкт-Петербургская"
        )
    }

    @Before
    fun start() {
        mockWebServer = MockWebServer()
        addressesService = createAddressesService()
    }

    @After
    fun shutDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun receiveTheFirstPageOfAddressesFromTheServerTest() {
        val response = runBlocking {
            addressesService.get(
                authorization = "Bearer $token",
                page = 0,
                size = 10
            )
        }

        assertNotNull(response.data)
    }

    @Test
    fun createTheNewAddressTest() {
        val expectedSizeOfAddressesList = runBlocking {
            addressesService.get(
                authorization = "Bearer $token",
                page = 0,
                size = 10
            )
        }.data?.sizeResult ?: 0

        runBlocking {
            addressesService.create(
                authorization = "Bearer $token",
                request = CreateAddressRequest(
                    city = cities[cities.indices.random()],
                    street = streets[streets.indices.random()],
                    house = "${(0..128).random()}",
                    title = "не будет тут описания"
                )
            )
        }

        val updatedSizeOfAddressesList = runBlocking {
            addressesService.get(
                authorization = "Bearer $token",
                page = 0,
                size = 10
            )
        }.data?.sizeResult

        assertEquals(expectedSizeOfAddressesList + 1, updatedSizeOfAddressesList)
    }

    @Test
    fun deleteAddressByUUIDTest() {
        val firstReceivedResponseOfAddressesData = runBlocking {
            addressesService.get(
                authorization = "Bearer $token",
                page = 0,
                size = 10
            )
        }.data

        assertNotNull(firstReceivedResponseOfAddressesData)

        val addresses = firstReceivedResponseOfAddressesData?.result ?: emptyList()
        val randomUUID = addresses[(addresses.indices).random()].id
        val deleteAddressResponse =
            runBlocking { addressesService.delete(authorization = "Bearer $token", randomUUID) }

        val secondReceivedResponseOfAddressesData = runBlocking {
            addressesService.get(
                authorization = "Bearer $token",
                page = 0,
                size = 10
            )
        }.data

        val addressesSize = (firstReceivedResponseOfAddressesData?.sizeResult ?: 0) - 1
        val addressesSizeBeforeDeleting = (secondReceivedResponseOfAddressesData?.sizeResult ?: 0)

        assertEquals(true, deleteAddressResponse.data)
        assertNotNull(secondReceivedResponseOfAddressesData)
        assertEquals(addressesSize, addressesSizeBeforeDeleting)
    }

    @Test
    fun updateAddressTest() {
        val updateAddressRequest = UpdateAddressRequest(
            city = cities[cities.indices.random()],
            street = streets[streets.indices.random()],
            house = "${(0..128).random()}"
        )

        val addresses = runBlocking {
            addressesService.get(
                authorization = "Bearer $token",
                page = 0,
                size = 10
            )
        }.data?.result ?: emptyList()

        val randomAddress = addresses[addresses.indices.random()]

        runBlocking {
            addressesService.update(
                authorization = "Bearer $token",
                request = updateAddressRequest,
                uuid = randomAddress.id
            )
        }

        val updatedAddress = runBlocking {
            addressesService.receiveAddressByID(
                authorization = "Bearer $token",
                uuid = randomAddress.id
            )
        }.data

        assertNotNull(updatedAddress)
        assertNotSame(randomAddress, updatedAddress)
    }

    private fun createAddressesService(): AddressesService {
        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://mobile-backend.cheese.asg-demo.ru/api/v1/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(AddressesService::class.java)
    }
}