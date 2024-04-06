@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package ru.antares.cheese_android.presentation.view.main.community_graph.community

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
                posts = (1..5).map {
                    PostModel(
                        id = "$it",
                        title = "title",
                        description = "description",
                        subtitle = "subtitle",
                        createdAt = "createdAt",
                        publishedAt = "publishedAt",
                        activityModel = ActivityModel(
                            id = "$it",
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
                            ticketPrice = "",
                            amountOfTicket = 0,
                            ticketsLeft = 0
                        ),
                        products = emptyList(),
                        posts = emptyList(),
                    )
                }
            )
        )
    }
}

@Preview
@Composable
fun CommunityItemViewPreview() {
    CheeseTheme {
        CommunityItemView(
            model = PostModel(
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
                    ticketPrice = "",
                    amountOfTicket = 0,
                    ticketsLeft = 0
                ),
                products = emptyList(),
                posts = emptyList(),
            ),
            onClick = {

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
    val canScrollBackward = remember { derivedStateOf { listState.canScrollBackward } }
    val coroutineScope = rememberCoroutineScope()

    CheeseTopAppBar(
        floatingActionButton = {
            AnimatedVisibility(
                visible = canScrollBackward.value,
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
                    state = state
                )
            }
        }
    }
}

@Composable
private fun CommunityScreenContent(
    lazyColumnState: LazyListState,
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
                    ?.contains(search.trim().lowercase()) ?: false
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
            items(
                items = posts,
                key = { it.id }
            ) { post ->
                CommunityItemView(
                    modifier = Modifier
                        .animateItemPlacement(animationSpec = tween(254)),
                    model = post,
                    onClick = { model ->
                        onNavigationEvent(CommunityNavigationEvent.NavigateToPost(model))
                    }
                )
            }
        }
    }
}

@Composable
private fun CommunityItemView(
    modifier: Modifier = Modifier,
    model: PostModel,
    onClick: (PostModel) -> Unit
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

    model.activityModel?.let {
        Box(
            modifier = modifier
                .aspectRatio(328f / 223f)
                .clickable(
                    scale = communityAnimatedScale,
                    onPressedChange = onPressedChange,
                    onClick = {
                        onClick(model)
                    }
                )
                .clip(CheeseTheme.shapes.medium)
                .alpha(communityAnimatedAlpha),
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model.imageURL)
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
                    text = model.activityModel.event.title,
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
                            text = model.activityModel.address,
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
                            text = model.activityModel.startFrom,
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
}
