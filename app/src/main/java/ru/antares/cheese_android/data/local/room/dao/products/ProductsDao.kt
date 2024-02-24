package ru.antares.cheese_android.data.local.room.dao.products

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * ProductsDao.kt
 * @author Павел
 * Created by on 23.02.2024 at 16:27
 * Android studio
 */

@Dao
interface ProductsDao {
    @Query("SELECT * FROM products")
    fun subscribeProductsFlow(): Flow<List<ProductWithCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: ProductEntity)

    @Query("DELETE FROM products")
    suspend fun clear()
}