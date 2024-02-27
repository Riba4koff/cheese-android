package ru.antares.cheese_android.data.local.room.dao.catalog

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories")
    fun subscribeCategoryFlow(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE categories.id = :id")
    suspend fun get(id: String): CategoryEntity

    @Query("SELECT * FROM categories WHERE categories.parentID = :parentID")
    suspend fun getByParentID(parentID: String): CategoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: CategoryEntity)

    @Query("DELETE FROM categories WHERE categories.id = :id")
    suspend fun remove(id: String)

    @Query("DELETE FROM categories")
    suspend fun clear()
}