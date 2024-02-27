package ru.antares.cheese_android.data.local.room.dao.catalog

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.antares.cheese_android.data.remote.dto.CategoryDTO
import ru.antares.cheese_android.data.remote.dto.toEntity

interface ICategoriesLocalStorage {
    suspend fun subscribeToCategoriesFlow(): Flow<List<CategoryEntity>>
    suspend fun insert(categories: List<CategoryDTO>)
}

class CategoriesLocalStorage(
    private val categoriesDao: CategoriesDao
): ICategoriesLocalStorage {
    override suspend fun subscribeToCategoriesFlow(): Flow<List<CategoryEntity>> = categoriesDao.subscribeCategoryFlow()

    override suspend fun insert(categories: List<CategoryDTO>) = withContext(Dispatchers.IO) {
        categories.forEach { category ->
            categoriesDao.insert(category.toEntity())
        }
    }
}