package ru.antares.cheese_android.data.local.room.dao.cart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * CartDao.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:15
 * Android studio
 */

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun subscribeCartFlow(): Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CartEntity)

    @Query("UPDATE cart SET amount = :amount WHERE productID == :productID")
    suspend fun update(amount: Int, productID: String)

    @Query("DELETE FROM cart WHERE productID == :productID")
    suspend fun delete(productID: String)

    @Query("DELETE FROM cart")
    suspend fun clear()
}