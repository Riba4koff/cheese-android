package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import ru.antares.cheese_android.domain.errors.AppError

sealed interface ProductsAppError: AppError {
    data class LoadingError(
        override val message: String = "Не удалось загрузить продукты"
    ): ProductsAppError
}