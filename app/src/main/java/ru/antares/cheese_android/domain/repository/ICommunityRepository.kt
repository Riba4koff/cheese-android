package ru.antares.cheese_android.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.api.community.CommunityError
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.domain.result.CheeseResult

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 15:26
 * Android Studio
 */

interface ICommunityRepository {
    suspend fun get(
        page: Int? = null,
        size: Int? = null,
        sort: String? = null,
        hasActivity: Boolean? = null
    ): Flow<CheeseResult<CommunityError, Pagination<PostModel>>>

    suspend fun get(
        id: String
    ): Flow<CheeseResult<CommunityError, PostModel>>
}