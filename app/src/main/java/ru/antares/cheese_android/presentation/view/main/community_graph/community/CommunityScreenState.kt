package ru.antares.cheese_android.presentation.view.main.community_graph.community

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.models.community.PostModel

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 17:43
 * Android Studio
 */

@optics
@Immutable
data class CommunityScreenState(
    val loading: Boolean = true,
    val posts: List<PostModel>? = null,
    val loadingNextPage: Boolean = false,
    val endReached: Boolean = false,
    val currentPage: Int = 0,
    val pageSize: Int = 16
) { companion object }