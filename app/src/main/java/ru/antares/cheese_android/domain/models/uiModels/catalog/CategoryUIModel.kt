package ru.antares.cheese_android.domain.models.uiModels.catalog

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryUIModel(
    val id: String = "",
    val name: String = "",
    val position: Int = 0,
    val parentID: String? = null
)
