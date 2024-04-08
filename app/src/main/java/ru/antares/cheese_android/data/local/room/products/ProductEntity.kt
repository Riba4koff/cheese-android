package ru.antares.cheese_android.data.local.room.products

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.antares.cheese_android.data.local.room.cart.CartEntity
import ru.antares.cheese_android.data.local.room.catalog.CategoryEntity

/**
 * ProductEntity.kt
 * @author Павел
 * Created by on 23.02.2024 at 16:28
 * Android studio
 */

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
        )
    ]
)
data class ProductEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("price") val price: Double,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("unit") val unit: Int,
    @ColumnInfo("categoryId") val categoryId: String,
    @ColumnInfo("recommend") val recommend: Boolean,
    @ColumnInfo("outOfStock") val outOfStock: Boolean,
    @ColumnInfo("unitName") val unitName: String
)