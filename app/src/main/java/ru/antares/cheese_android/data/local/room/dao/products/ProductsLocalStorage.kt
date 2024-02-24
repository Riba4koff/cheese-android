package ru.antares.cheese_android.data.local.room.dao.products

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.antares.cheese_android.data.local.room.dao.catalog.CategoryDao
import ru.antares.cheese_android.data.local.room.dao.catalog.toCategoryUIModel
import ru.antares.cheese_android.data.remote.dto.ProductDTO
import ru.antares.cheese_android.data.remote.dto.toEntity
import ru.antares.cheese_android.domain.models.ProductModel

/**
 * ProductsLocalStorage.kt
 * @author Павел
 * Created by on 23.02.2024 at 17:00
 * Android studio
 */

interface IProductsLocalStorage {
    fun products(): Flow<List<ProductModel>>
    suspend fun insert(products: List<ProductDTO>)
}

class ProductsLocalStorage(
    private val productsDao: ProductsDao,
    private val categoryDao: CategoryDao
): IProductsLocalStorage {
    override fun products(): Flow<List<ProductModel>> = productsDao.subscribeProductsFlow().map { productWithCategories ->
        productWithCategories.map { productWithCategory ->
            val productEntity = productWithCategory.product
            val categoryModel = productWithCategory.category.toCategoryUIModel()

            ProductModel(
                id = productEntity.id,
                name = productEntity.name,
                price = productEntity.price,
                description = productEntity.description,
                unit = productEntity.unit,
                category = categoryModel,
                recommend = productEntity.recommend,
                outOfStock = productEntity.outOfStock,
                unitName = productEntity.unitName,
                categoryId = categoryModel.id,
                categories = listOf(categoryModel)
            )
        }
    }

    override suspend fun insert(products: List<ProductDTO>) {
        products.forEach { dto ->
            val categoryEntity = dto.category.toEntity()
            val productEntity = dto.toEntity()
            categoryDao.insert(categoryEntity)
            productsDao.insert(productEntity)
        }
    }
}