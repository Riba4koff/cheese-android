package ru.antares.cheese_android.data.repository.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.local.room.addresses.IAddressesLocalStorage
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.models.map
import ru.antares.cheese_android.data.remote.api.main.addresses.AddressesError
import ru.antares.cheese_android.data.remote.api.main.addresses.AddressesApiHandler
import ru.antares.cheese_android.data.remote.api.main.addresses.request.CreateAddressRequest
import ru.antares.cheese_android.data.remote.api.main.addresses.request.UpdateAddressRequest
import ru.antares.cheese_android.domain.repository.IAddressesRepository
import ru.antares.cheese_android.domain.result.CheeseResult
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 15:34
 * Android Studio
 */

class AddressesRepository(
    private val handler: AddressesApiHandler,
    private val localStorage: IAddressesLocalStorage
) : IAddressesRepository {
    override suspend fun get(
        page: Int,
        pageSize: Int,
        sortByColumn: String?,
        sortDirection: String?
    ): Flow<CheeseResult<AddressesError, Pagination<AddressModel>>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get(
            page = page,
            size = pageSize,
            sortDirection = sortDirection,
            sortByColumn = sortByColumn
        ).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { pagination ->
            pagination.map { it.toEntity() }.result.forEach { entity ->
                localStorage.insert(entity)
            }
            emit(CheeseResult.Success(pagination.map { it.toModel() }))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }

    override suspend fun get(id: String): Flow<CheeseResult<AddressesError, AddressModel>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get(id).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { address ->
            emit(CheeseResult.Success(address.toModel()))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }

    override suspend fun create(request: CreateAddressRequest): Flow<CheeseResult<AddressesError, AddressModel>> =
        flow {
            emit(CheeseResult.Loading(isLoading = true))

            handler.create(request).onError { error ->
                emit(CheeseResult.Error(error))
            }.onSuccess { address ->
                localStorage.insert(address.toEntity())
                emit(CheeseResult.Success(address.toModel()))
            }

            emit(CheeseResult.Loading(isLoading = false))
        }

    override suspend fun update(
        id: String,
        request: UpdateAddressRequest
    ): Flow<CheeseResult<AddressesError, AddressModel>> =
        flow {
            emit(CheeseResult.Loading(isLoading = true))

            handler.update(id, request).onError { error ->
                emit(CheeseResult.Error(error))
            }.onSuccess { address ->
                localStorage.insert(address.toEntity())
                emit(CheeseResult.Success(address.toModel()))
            }

            emit(CheeseResult.Loading(isLoading = false))
        }

    override suspend fun remove(id: String): Flow<CheeseResult<AddressesError, Unit>> =
        flow {
            emit(CheeseResult.Loading(isLoading = true))

            handler.delete(id).onError { error ->
                emit(CheeseResult.Error(error))
            }.onSuccess { _ ->
                localStorage.remove(id)
                emit(CheeseResult.Success(Unit))
            }

            emit(CheeseResult.Loading(isLoading = false))
        }
}