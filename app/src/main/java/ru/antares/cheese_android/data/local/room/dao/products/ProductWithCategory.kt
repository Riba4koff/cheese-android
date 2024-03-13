package ru.antares.cheese_android.data.local.room.dao.products

import androidx.room.Embedded
import androidx.room.Relation
import ru.antares.cheese_android.data.local.room.dao.catalog.CategoryEntity

/**
 * ProductWithCategory.kt
 * @author Павел
 * Created by on 23.02.2024 at 16:48
 * Android studio
 */


data class ProductWithCategory(
    @Embedded
    val product: ProductEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)
