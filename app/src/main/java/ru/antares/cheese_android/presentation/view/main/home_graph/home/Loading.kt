package ru.antares.cheese_android.presentation.view.main.home_graph.home

/**
 * Loading.kt
 * @author Павел
 * Created by on 06.05.2024 at 21:13
 * Android studio
 */

sealed interface LoadingPosts {
    data object Initial: LoadingPosts
    data object LoadingNextPage: LoadingPosts
    data object Loaded: LoadingPosts
}

sealed interface LoadingActivities {
    data object Initial: LoadingActivities
    data object LoadingNextPage: LoadingActivities
    data object Loaded: LoadingActivities
}

sealed interface LoadingRecommendations {
    data object Initial: LoadingRecommendations
    data object LoadingNextPage: LoadingRecommendations
    data object Loaded: LoadingRecommendations
}