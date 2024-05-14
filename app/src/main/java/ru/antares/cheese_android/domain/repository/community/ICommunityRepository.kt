package ru.antares.cheese_android.domain.repository.community

import ru.antares.cheese_android.data.remote.models.Pagination
import ru.antares.cheese_android.data.remote.api.main.community.PostDTO
import ru.antares.cheese_android.domain.Result

/**
 * ICommunityRepository.kt
 * @author Павел
 * Created by on 02.04.2024 at 19:22
 * Android studio
 */

interface ICommunityRepository {
    suspend fun get(
        page: Int,
        pageSize: Int
    ): Result<CommunityError, Pagination<PostDTO>>
}