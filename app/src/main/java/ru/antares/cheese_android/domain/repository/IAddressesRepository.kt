package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.services.addresses.AddressesError
import ru.antares.cheese_android.data.remote.services.addresses.request.CreateAddressRequest
import ru.antares.cheese_android.data.remote.services.addresses.request.UpdateAddressRequest
import ru.antares.cheese_android.domain.result.CheeseResult
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 15:30
 * Android Studio
 */

interface IAddressesRepository {
    suspend fun get(
        page: Int,
        pageSize: Int,
        sortByColumn: String? = null,
        sortDirection: String? = null
    ): Flow<CheeseResult<AddressesError, Pagination<AddressModel>>>

    suspend fun get(
        id: String
    ): Flow<CheeseResult<AddressesError, AddressModel>>

    suspend fun create(
        request: CreateAddressRequest
    ): Flow<CheeseResult<AddressesError, AddressModel>>

    suspend fun update(
        id: String,
        request: UpdateAddressRequest
    ): Flow<CheeseResult<AddressesError, AddressModel>>

    suspend fun remove(
        id: String
    ): Flow<CheeseResult<AddressesError, Unit>>
}