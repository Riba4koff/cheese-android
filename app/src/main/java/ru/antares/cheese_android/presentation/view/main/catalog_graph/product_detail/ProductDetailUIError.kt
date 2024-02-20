package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

import ru.antares.cheese_android.domain.errors.UIError

sealed interface ProductDetailUIError: UIError {
    data class LoadingError(
        override val message: String = "Не удалось загрузить\nинформацию о продукте"
    ): ProductDetailUIError
}