package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import ru.antares.cheese_android.domain.errors.UIError

sealed interface ProductsUIError: UIError {
    data class LoadingError(
        override val message: String = "Не удалось загрузить продукты"
    ): ProductsUIError
}