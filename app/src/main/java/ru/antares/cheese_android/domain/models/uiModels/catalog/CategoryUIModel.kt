package ru.antares.cheese_android.domain.models.uiModels.catalog

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryUIModel(
    val id: String = "",
    val name: String = "",
    val position: Int = 0,
    val parentID: String? = null
) {
    val imageLink: String
        get() = "https://mobile-backend.cheese.asg-demo.ru/api/v1/store/categories/${id}/image"
}
