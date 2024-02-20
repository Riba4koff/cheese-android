package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import ru.antares.cheese_android.domain.errors.UIError

sealed interface CatalogUIError : UIError {
    data class Loading(
        override val message: String = "Ошибка при загрузке категорий"
    ) : CatalogUIError

    data class UnknownError(
        override val message: String = "Неизвестная ошибка"
    ) : CatalogUIError

    data class Updating(
        override val message: String = "Ошибка при обновлении категорий"
    ) : CatalogUIError
}