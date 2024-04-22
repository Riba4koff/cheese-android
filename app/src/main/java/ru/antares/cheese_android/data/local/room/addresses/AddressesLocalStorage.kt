package ru.antares.cheese_android.data.local.room.addresses

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.presentation.models.AddressModel

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 15:55
 * Android Studio
 */

interface IAddressesLocalStorage {
    fun get(): Flow<List<AddressModel>>
    suspend fun get(id: String): AddressModel?
    suspend fun insert(value: AddressEntity)
    suspend fun clear(): Unit
    suspend fun remove(id: String): Unit
}

class AddressesLocalStorage(
    private val dao: AddressesDao
): IAddressesLocalStorage {
    override fun get(): Flow<List<AddressModel>> = dao.get().map { entities ->
        entities.map { it.toModel() }
    }.flowOn(Dispatchers.IO)

    override suspend fun get(id: String): AddressModel? {
        return withContext(Dispatchers.IO) {
            dao.get(id)?.toModel()
        }
    }

    override suspend fun insert(value: AddressEntity) {
        return withContext(Dispatchers.IO) {
            dao.insert(value)
        }
    }

    override suspend fun clear() {
        return withContext(Dispatchers.IO) {
            dao.clear()
        }
    }

    override suspend fun remove(id: String) {
        return withContext(Dispatchers.IO) {
            dao.remove(id)
        }
    }
}