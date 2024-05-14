package ru.antares.cheese_android.presentation.view.main.home_graph.home

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * HomeScreenState.kt
 * @author Павел
 * Created by on 06.05.2024 at 21:02
 * Android studio
 */

@optics
@Immutable
data class HomeScreenState(
    // state of posts
    val loadingPosts: LoadingPosts = LoadingPosts.Initial,
    val posts: List<PostModel> = emptyList(),

    // state of activities
    val loadingActivities: LoadingActivities = LoadingActivities.Initial,
    val activities: List<PostModel> = emptyList(),

    // state of recommendations
    val loadingRecommendations: LoadingRecommendations = LoadingRecommendations.Initial,
    val recommendations: List<ProductUIModel> = emptyList()
) { companion object }