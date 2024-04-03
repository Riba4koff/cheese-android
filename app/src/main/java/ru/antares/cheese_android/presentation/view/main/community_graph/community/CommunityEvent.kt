package ru.antares.cheese_android.presentation.view.main.community_graph.community

import ru.antares.cheese_android.domain.models.community.PostModel

/**
 * @author pavelrybakov
 * Created 03.04.2024 at 17:45
 * Android Studio
 */

sealed interface CommunityEvent {
    data class LoadNextPage(val page: Int?, val size: Int?) : CommunityEvent
}