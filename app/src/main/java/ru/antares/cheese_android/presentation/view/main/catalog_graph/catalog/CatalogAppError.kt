package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import ru.antares.cheese_android.domain.errors.AppError

sealed interface CatalogAppError : AppError {
    data class Loading(
        override val message: String = "Ошибка при загрузке категорий"
    ) : CatalogAppError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ) : CatalogAppError

    data class Updating(
        override val message: String = "Ошибка при обновлении категорий"
    ) : CatalogAppError
}