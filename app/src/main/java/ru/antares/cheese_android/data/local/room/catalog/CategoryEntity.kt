package ru.antares.cheese_android.data.local.room.catalog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.antares.cheese_android.domain.models.CategoryModel

@Entity("categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("position") val position: Int,
    @ColumnInfo("parentID") val parentID: String?
)

fun CategoryEntity.toCategoryUIModel() = CategoryModel(
    id = id,
    name = name,
    position = position,
    parentID = parentID
)