package ru.antares.cheese_android.data.local.room.dao.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.antares.cheese_android.data.local.room.dao.products.ProductEntity

/**
 * CartEntity.kt
 * @author Павел
 * Created by on 23.02.2024 at 15:14
 * Android studio
 */

@Entity(
    "cart",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productID"]
        )
    ]
)
data class CartEntity(
    @PrimaryKey val productID: String,
    @ColumnInfo("amount") val amount: Int
)
