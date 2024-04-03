package ru.antares.cheese_android.presentation.view.main.community_graph.community

import androidx.compose.runtime.Immutable
import ru.antares.cheese_android.domain.models.community.PostModel

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 17:43
 * Android Studio
 */

@Immutable
data class CommunityScreenState(
    val loading: Boolean = false,
    val posts: List<PostModel>? = null
)