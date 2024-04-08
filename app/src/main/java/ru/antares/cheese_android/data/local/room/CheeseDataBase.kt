package ru.antares.cheese_android.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.antares.cheese_android.data.local.room.cart.CartDao
import ru.antares.cheese_android.data.local.room.cart.CartEntity
import ru.antares.cheese_android.data.local.room.catalog.CategoriesDao
import ru.antares.cheese_android.data.local.room.catalog.CategoryEntity
import ru.antares.cheese_android.data.local.room.products.ProductEntity
import ru.antares.cheese_android.data.local.room.products.ProductsDao

/**
 * @author Pavel Rybakov
 * */

@Database(
    entities = [
        CategoryEntity::class,
        CartEntity::class,
        ProductEntity::class
    ],
    version = 9,
    exportSchema = false
)
abstract class CheeseDataBase : RoomDatabase() {
    abstract fun catalogDao(): CategoriesDao
    abstract fun productsDao(): ProductsDao
    abstract fun cartDao(): CartDao

    companion object {
        const val DB_NAME = "Cheese_DB"
    }
}