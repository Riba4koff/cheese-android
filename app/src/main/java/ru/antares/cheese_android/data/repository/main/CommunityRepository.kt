package ru.antares.cheese_android.data.repository.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.local.room.catalog.ICategoriesLocalStorage
import ru.antares.cheese_android.data.local.room.products.IProductsLocalStorage
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.models.map
import ru.antares.cheese_android.data.remote.services.community.CommunityServiceHandler
import ru.antares.cheese_android.data.remote.services.community.CommunityError
import ru.antares.cheese_android.data.remote.services.community.dto.PostDTO
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.domain.repository.ICommunityRepository
import ru.antares.cheese_android.domain.result.CheeseResult

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:26
 * Android Studio
 */

class CommunityRepository(
    private val handler: CommunityServiceHandler,
    private val productsLocalStorage: IProductsLocalStorage,
    private val categoriesLocalStorage: ICategoriesLocalStorage
) : ICommunityRepository {
    override suspend fun get(
        page: Int?,
        size: Int?,
        sort: String?,
        hasActivity: Boolean?
    ): Flow<CheeseResult<CommunityError, Pagination<PostModel>>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get(size, page, sort, hasActivity).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { data: Pagination<PostDTO> ->
            emit(CheeseResult.Success(data.map { it.toModel() }))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }

    override suspend fun get(id: String): Flow<CheeseResult<CommunityError, PostModel>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get(id).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { post ->
            productsLocalStorage.insert(post.products)
            categoriesLocalStorage.insert(post.products.map { it.category })
            emit(CheeseResult.Success(data = post.toModel()))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }
}