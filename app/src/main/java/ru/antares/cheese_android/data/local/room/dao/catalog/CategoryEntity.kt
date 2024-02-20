package ru.antares.cheese_android.data.local.room.dao.catalog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel

@Entity("categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("position") val position: Int,
    @ColumnInfo("parentID") val parentID: String?
)

fun CategoryEntity.toCategoryUIModel() = CategoryUIModel(
    id = id,
    name = name,
    position = position,
    parentID = parentID
)