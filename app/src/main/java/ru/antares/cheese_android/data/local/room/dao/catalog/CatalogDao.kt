package ru.antares.cheese_android.data.local.room.dao.catalog

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {
    @Query("SELECT * FROM categories")
    fun categoryEntityFlow(): Flow<CategoryEntity>

    @Query("SELECT * FROM categories WHERE categories.id = :id")
    suspend fun getByID(id: String): CategoryEntity

    @Query("SELECT * FROM categories WHERE categories.parentID = :parentID")
    suspend fun getByParentID(parentID: String): CategoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: CategoryEntity): Unit

    @Query("DELETE FROM categories WHERE categories.id = :id")
    suspend fun remove(id: String): Unit

    @Query("DELETE FROM categories")
    suspend fun clear(): Unit
}