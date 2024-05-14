package ru.antares.cheese_android.data.remote.api.main.community

data class Category(
    val id: String,
    val name: String,
    val parentID: String,
    val position: Int
)