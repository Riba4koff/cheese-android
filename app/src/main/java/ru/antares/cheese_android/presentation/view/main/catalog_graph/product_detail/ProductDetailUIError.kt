package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

import ru.antares.cheese_android.domain.errors.UIError

/**
 * ProductDetailUIError.kt
 * @author Павел
 * Created by on 22.02.2024 at 22:50
 * Android studio
 */

sealed interface ProductDetailUIError: UIError {
    data class LoadingError(
        override val message: String = "Не удалось загрузить\nинформацию о продукте"
    ): ProductDetailUIError
}