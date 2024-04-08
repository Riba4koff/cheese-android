package ru.antares.cheese_android.data.local.room.addresses

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.services.addresses.AddressDTO

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 15:47
 * Android Studio
 */

@Dao
interface AddressesDao {
    @Query("SELECT * FROM addresses")
    fun get(): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addresses WHERE id = :id")
    suspend fun get(id: String): AddressEntity?

    @Query("DELETE FROM addresses")
    suspend fun clear()

    @Query("DELETE FROM addresses WHERE id = :id")
    suspend fun remove(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(address: AddressEntity)
}