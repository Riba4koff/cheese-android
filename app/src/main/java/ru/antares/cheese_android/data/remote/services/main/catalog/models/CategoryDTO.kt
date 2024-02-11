package ru.antares.cheese_android.data.remote.services.main.catalog.models

data class CategoryDTO(
    val id: String,
    val name: String,
    val position: Int,
    val parentID: String? = null
)
