@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package ru.antares.cheese_android.presentation.view.main.home_graph.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.domain.models.community.ActivityModel
import ru.antares.cheese_android.domain.models.community.EventModel
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.presentation.components.ShimmerLoadingBox
import ru.antares.cheese_android.presentation.components.topbars.CheeseTopAppBar
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductView
import ru.antares.cheese_android.presentation.view.main.community_graph.community.ActivityItemView
import ru.antares.cheese_android.presentation.view.main.community_graph.community.PostItemView
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CheeseTheme {
        HomeScreen(
            state = HomeScreenState(
                loadingPosts = LoadingPosts.Initial
            ),
            onEvent = {},
            onNavigationEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHorizontalPagerPreview() {
    val state = LoadingActivities.Initial

    CheeseTheme {
        HomeHorizontalPager(
            isLoading = state == LoadingActivities.Initial,
            title = "Блог",
            items = listOf("1", "2", "3", "4", "5"),
            pagerState = rememberPagerState {
                5
            }
        ) {
            Box(modifier = Modifier.aspectRatio(2f / 1f)) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("")
                        .crossfade(true)
                        .build(),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    state: HomeScreenState,
    onEvent: (HomeScreenEvent) -> Unit,
    onNavigationEvent: (HomeScreenNavigationEvent) -> Unit
) {
    val upcomingEventsTitle = "Ближайшие мероприятия"
    val recommendationsTitle = "Рекомендации"
    val blogTitle = "Блог"

    val activitiesPagerState = rememberPagerState {
        if (state.activities.isEmpty()) 1 else state.activities.size
    }
    val postsPagerState = rememberPagerState {
        if (state.posts.isEmpty()) 1 else state.posts.size
    }
    val recommendationsPagerState = rememberPagerState {
        if (state.recommendations.isEmpty()) 1 else state.recommendations.size
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5_000L)

            val nextPage =
                if (activitiesPagerState.currentPage == activitiesPagerState.pageCount - 1) 0 else activitiesPagerState.currentPage + 1
            activitiesPagerState.animateScrollToPage(
                nextPage,
                animationSpec = spring(dampingRatio = 1.5f)
            )
        }
    }

    CheeseTopAppBar(title = stringResource(id = R.string.home_title)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = CheeseTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
        ) {
            HomeHorizontalPager(
                isLoading = state.loadingPosts == LoadingPosts.Initial,
                pagerState = postsPagerState,
                title = upcomingEventsTitle,
                items = state.posts
            ) { post ->
                PostItemView(post = post) { _ ->
                    onNavigationEvent(HomeScreenNavigationEvent.NavigateToPost(post.id))
                }
            }
            HomeHorizontalPager(
                isLoading = state.loadingRecommendations == LoadingRecommendations.Initial,
                pagerState = recommendationsPagerState,
                title = recommendationsTitle,
                items = state.recommendations
            ) { recommendation ->
                ProductView(
                    product = recommendation,
                    onClick = { _ ->
                        onNavigationEvent(
                            HomeScreenNavigationEvent.NavigateToRecommendation(
                                recommendation.value.id
                            )
                        )
                    }, addToCart = { _ ->
                        onEvent(
                            HomeScreenRecommendationsEvent.AddToCart(
                                recommendation.value.id,
                                recommendation.countInCart
                            )
                        )
                    }, removeFromCart = { _ ->
                        onEvent(
                            HomeScreenRecommendationsEvent.RemoveFromCart(
                                recommendation.value.id,
                                recommendation.countInCart
                            )
                        )
                    }
                )
            }
            HomeHorizontalPager(
                isLoading = state.loadingPosts == LoadingPosts.Initial,
                pagerState = activitiesPagerState,
                title = blogTitle,
                items = state.activities
            ) { post ->
                post.activityModel?.let { activity ->
                    ActivityItemView(
                        modifier = Modifier,
                        postID = post.id,
                        model = activity,
                        imageUrl = post.imageURL,
                        onClickToPost = { activityID: String ->
                            onNavigationEvent(HomeScreenNavigationEvent.NavigateToActivity(activityID))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun <T> HomeHorizontalPager(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    title: String,
    contentPadding: PaddingValues = PaddingValues(horizontal = CheeseTheme.paddings.medium),
    items: List<T>,
    pagerState: PagerState,
    item: @Composable (T) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
    ) {
        Text(
            modifier = Modifier
                .padding(contentPadding),
            text = title,
            style = CheeseTheme.typography.common24Semibold
        )
        HorizontalPager(
            state = pagerState,
            pageSpacing = CheeseTheme.paddings.medium,
            contentPadding = contentPadding
        ) { page ->
            Box(modifier = Modifier.aspectRatio(328f / 223f)) {
                this@Column.AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ShimmerLoadingBox(
                        modifier = Modifier
                            .aspectRatio(328f / 223f),
                        shape = CheeseTheme.shapes.medium
                    )
                }
                this@Column.AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    if (items.isNotEmpty()) {
                        item(items[page])
                    }
                }
            }
        }
    }
}