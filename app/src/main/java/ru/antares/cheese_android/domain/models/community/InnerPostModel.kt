package ru.antares.cheese_android.domain.models.community

import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:44
 * Android Studio
 */

@Immutable
data class InnerPostModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val createdAt: String,
    val categories: List<CategoryModel>,
    val category: CategoryModel,
    val products: List<ProductModel>,
)