package ru.antares.cheese_android.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.antares.cheese_android.data.local.room.dao.catalog.CatalogDao
import ru.antares.cheese_android.data.local.room.dao.catalog.CategoryEntity

/**
 * @author Pavel Rybakov
 * */

@Database(
    entities = [
        CategoryEntity::class
    ],
    version = 0,
    exportSchema = false
)
abstract class CheeseDataBase: RoomDatabase() {

    abstract fun catalogDao(): CatalogDao

    companion object {
        const val DB_NAME = "Cheese_DB"
    }
}