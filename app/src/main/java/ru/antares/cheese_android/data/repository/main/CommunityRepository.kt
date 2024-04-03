package ru.antares.cheese_android.data.repository.main

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.models.map
import ru.antares.cheese_android.data.remote.services.community.CommunityServiceHandler
import ru.antares.cheese_android.data.remote.services.community.GetCommunityError
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
    private val handler: CommunityServiceHandler
) : ICommunityRepository {
    override suspend fun get(
        page: Int?,
        size: Int?,
        sort: String?
    ): Flow<CheeseResult<GetCommunityError, Pagination<PostModel>>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get(size, page, sort).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { data: Pagination<PostDTO> ->
            emit(CheeseResult.Success(data.map { it.toModel() }))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }

    override suspend fun get(id: String): Flow<CheeseResult<GetCommunityError, PostModel>> = flow {
        emit(CheeseResult.Loading(isLoading = true))

        handler.get(id).onError { error ->
            emit(CheeseResult.Error(error))
        }.onSuccess { post ->
            emit(CheeseResult.Success(post.toModel()))
        }

        emit(CheeseResult.Loading(isLoading = false))
    }
}