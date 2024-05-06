@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package ru.antares.cheese_android.presentation.view.main.community_graph.community

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import ru.antares.cheese_android.R
import ru.antares.cheese_android.clickable
import ru.antares.cheese_android.domain.models.community.ActivityModel
import ru.antares.cheese_android.domain.models.community.EventModel
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.textfields.CheeseSearchTextField
import ru.antares.cheese_android.presentation.components.topbars.CheeseTopAppBar
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun CommunityScreenPreview() {
    CheeseTheme {
        CommunityScreen(
            onEvent = {},
            onNavigationEvent = {},
            state = CommunityScreenState(
                loading = false,
                posts = listOf(
                    PostModel(
                        id = "1",
                        title = "title",
                        description = "description",
                        subtitle = "subtitle",
                        createdAt = "createdAt",
                        publishedAt = "publishedAt",
                        activityModel = ActivityModel(
                            id = "1",
                            event = EventModel(
                                id = "1",
                                title = "Сырная тусовка",
                                description = "description",
                            ),
                            startFrom = "04.08.2023 20:00",
                            longitude = 0.0,
                            latitude = 0.0,
                            address = "Ломоносова 16. ТЦ Мармелад",
                            addressDescription = "",
                            ticketPrice = 15000.0,
                            amountOfTicket = 0,
                            ticketsLeft = 0
                        ),
                        products = emptyList(),
                        posts = emptyList(),
                    ),
                    PostModel(
                        id = "2",
                        title = "title",
                        description = "description",
                        subtitle = "subtitle",
                        createdAt = "createdAt",
                        publishedAt = "publishedAt",
                        activityModel = null,
                        products = emptyList(),
                        posts = emptyList(),
                    )
                ),
                loadingNextPage = true
            )
        )
    }
}

@Preview
@Composable
fun ActivityItemViewPreview() {
    CheeseTheme {
        ActivityItemView(
            imageUrl = "",
            postID = "",
            model = ActivityModel(
                id = "1",
                event = EventModel(
                    id = "1",
                    title = "Сырная тусовка",
                    description = "description",
                ),
                startFrom = "04.08.2023 20:00",
                longitude = 0.0,
                latitude = 0.0,
                address = "Ломоносова 16. ТЦ Мармелад",
                addressDescription = "",
                ticketPrice = 15000.0,
                amountOfTicket = 0,
                ticketsLeft = 0
            ),
            onClickToPost = {

            }
        )
    }
}

@Preview
@Composable
fun PostItemViewPreview() {
    CheeseTheme {
        PostItemView(
            post = PostModel(
                id = "1",
                title = "Вкусные завтраки с сыром",
                description = "description",
                subtitle = "Чем и как нарезать сыр?",
                createdAt = "createdAt",
                publishedAt = "publishedAt",
                activityModel = null,
                products = emptyList(),
                posts = emptyList(),
            ), onClickToPost = {

            }
        )
    }
}

@Composable
fun CommunityScreen(
    onEvent: (CommunityEvent) -> Unit,
    onNavigationEvent: (CommunityNavigationEvent) -> Unit,
    state: CommunityScreenState
) {
    val listState = rememberLazyListState()
    val floatingActionButtonIsVisible = remember {
        derivedStateOf { listState.firstVisibleItemIndex >= 1 }
    }
    val coroutineScope = rememberCoroutineScope()

    CheeseTopAppBar(
        floatingActionButton = {
            AnimatedVisibility(
                visible = floatingActionButtonIsVisible.value,
                enter = fadeIn(tween(128)),
                exit = fadeOut(tween(128))
            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    shape = CheeseTheme.shapes.large,
                    containerColor = CheeseTheme.colors.accent
                ) {
                    Icon(imageVector = Icons.Outlined.KeyboardArrowUp, contentDescription = null)
                }
            }
        },
        title = stringResource(id = R.string.community_title)
    ) {
        AnimatedContent(
            targetState = state.loading,
            label = "Catalog screen animated content",
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(200)
                ).togetherWith(
                    fadeOut(
                        animationSpec = tween(200)
                    )
                )
            },
        ) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                CommunityScreenContent(
                    lazyColumnState = listState,
                    onNavigationEvent = onNavigationEvent,
                    onEvent = onEvent,
                    state = state
                )
            }
        }
    }
}

@Composable
private fun CommunityScreenContent(
    lazyColumnState: LazyListState,
    onEvent: (CommunityEvent) -> Unit,
    onNavigationEvent: (CommunityNavigationEvent) -> Unit,
    state: CommunityScreenState
) {
    val (search, onSearchChange) = remember { mutableStateOf("") }
    var posts by remember { mutableStateOf(state.posts) }

    LaunchedEffect(key1 = search, key2 = posts) {
        if (posts != null) {
            onSearchChange(search)
            posts = state.posts?.filter {
                it.activityModel?.event?.title?.lowercase()?.trim()
                    ?.contains(search.trim().lowercase()) ?: false ||
                        it.title.lowercase().trim().contains(search.trim().lowercase()) ||
                        it.subtitle.lowercase().trim().contains(search.trim().lowercase())
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = CheeseTheme.paddings.medium),
        state = lazyColumnState
    ) {
        posts?.let { posts ->
            item {
                CheeseSearchTextField(
                    modifier = Modifier
                        .padding(top = CheeseTheme.paddings.medium)
                        .fillMaxWidth(),
                    value = search,
                    onValueChange = onSearchChange
                )
            }
            itemsIndexed(
                items = posts,
                key = { _, it -> it.id }
            ) { index, item ->
                if (item.activityModel != null) {
                    ActivityItemView(
                        modifier = Modifier
                            .animateItemPlacement(animationSpec = tween(256)),
                        postID = item.id,
                        model = item.activityModel,
                        imageUrl = item.imageURL,
                        onClickToPost = { id ->
                            onNavigationEvent(CommunityNavigationEvent.NavigateToActivity(id))
                        }
                    )
                } else {
                    PostItemView(
                        modifier = Modifier
                            .animateItemPlacement(animationSpec = tween(256)),
                        post = item,
                        onClickToPost = { post ->
                            onNavigationEvent(CommunityNavigationEvent.NavigateToPost(post.id))
                        }
                    )
                }
                if (index >= posts.size - 1 && !state.loadingNextPage && !state.endReached) onEvent(
                    CommunityEvent.LoadNextPage(
                        page = state.currentPage + 1, size = state.pageSize
                    )
                )
            }
            item {
                AnimatedVisibility(
                    visible = state.loadingNextPage && !state.endReached,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    LoadingNextPageItemView(Modifier.animateItemPlacement(tween(256)))
                }
            }
        }
    }
}

@Composable
fun ActivityItemView(
    modifier: Modifier = Modifier,
    postID: String,
    model: ActivityModel,
    imageUrl: String,
    onClickToPost: (postID: String) -> Unit,
) {
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val communityAnimatedScale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        label = "Community view pressed animated scale"
    )
    val communityAnimatedAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.6f else 1f,
        label = "Community view animated alpha"
    )
    val verticalBlackGradient = arrayOf(
        0f to Color.Transparent,
        1f to Color.Black
    )

    Box(
        modifier = modifier
            .aspectRatio(328f / 223f)
            .clickable(
                scale = communityAnimatedScale,
                onPressedChange = onPressedChange,
                onClick = {
                    onClickToPost(postID)
                }
            )
            .clip(CheeseTheme.shapes.medium)
            .alpha(communityAnimatedAlpha),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colorStops = verticalBlackGradient))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(CheeseTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Text(
                text = model.event.title,
                style = CheeseTheme.typography.common20Semibold,
                color = CheeseTheme.colors.white
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = CheeseTheme.colors.accent
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = model.address,
                        style = CheeseTheme.typography.common12Medium,
                        color = CheeseTheme.colors.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = null,
                        tint = CheeseTheme.colors.accent
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = model.startFrom,
                        style = CheeseTheme.typography.common12Medium,
                        color = CheeseTheme.colors.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun PostItemView(
    modifier: Modifier = Modifier,
    post: PostModel,
    onClickToPost: (PostModel) -> Unit
) {
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val communityAnimatedScale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        label = "Community view pressed animated scale"
    )
    val communityAnimatedAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.6f else 1f,
        label = "Community view animated alpha"
    )
    val verticalBlackGradient = arrayOf(
        0f to Color.Transparent,
        1f to Color.Black
    )

    Box(
        modifier = modifier
            .aspectRatio(328f / 223f)
            .clickable(
                scale = communityAnimatedScale,
                onPressedChange = onPressedChange,
                onClick = {
                    onClickToPost(post)
                }
            )
            .clip(CheeseTheme.shapes.medium)
            .alpha(communityAnimatedAlpha),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.imageURL)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colorStops = verticalBlackGradient))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(CheeseTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = post.title,
                    style = CheeseTheme.typography.common20Semibold,
                    color = CheeseTheme.colors.white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = post.description,
                    style = CheeseTheme.typography.common12Regular,
                    color = CheeseTheme.colors.white,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Composable
private fun LoadingNextPageItemView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(328f / 223f)
            .clip(CheeseTheme.shapes.medium)
            .background(color = CheeseTheme.colors.lightGray)
    ) {
        LoadingIndicator(
            isLoading = true,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}