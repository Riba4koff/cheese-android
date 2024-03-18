package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_parent_category

import ru.antares.cheese_android.domain.errors.AppError

sealed interface CatalogParentCategoryAppError : AppError {
    data class Loading(
        override val message: String = "Не удалось загрузить категории"
    ) : CatalogParentCategoryAppError
}