package ru.antares.cheese_android.presentation.view.main.community_graph.activity

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.presentation.models.ProductUIModel

/**
 * CommunityDetailScreenState.kt
 * @author Павел
 * Created by on 06.04.2024 at 19:51
 * Android studio
 */

@optics
@Immutable
data class ActivityScreenState(
    val loading: Boolean = true,
    val post: PostModel? = null,
    val products: List<ProductUIModel> = emptyList(),
    val loadingCart: Boolean = false
) { companion object }