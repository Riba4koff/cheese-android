package ru.antares.cheese_android.data.local.room.dao.catalog

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CatalogDao {
    @Query("SELECT * FROM categories")
    fun get(): Flow<CategoryEntity>

    @Query("SELECT * FROM categories WHERE categories.id = :id")
    fun getByID(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: CategoryEntity)

    @Query("DELETE FROM categories WHERE categories.id = :id")
    fun remove(id: String)

    @Query("DELETE FROM categories")
    fun clear()
}