package ru.antares.cheese_android.presentation.view.main.profile_graph.tickets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.antares.cheese_android.R
import ru.antares.cheese_android.clickable
import ru.antares.cheese_android.domain.models.community.ActivityModel
import ru.antares.cheese_android.domain.models.community.EventModel
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 03.05.2024 at 13:59
 * Android Studio
 */

@Preview(showBackground = true)
@Composable
private fun TicketsScreenPreview() {
    CheeseTheme {
        TicketsScreen(
            state = TicketsState(loading = false)
        )
    }
}

@Preview()
@Composable
private fun TicketViewPreview() {
    CheeseTheme {
        TicketView(
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

@Composable
fun TicketsScreen(
    state: TicketsState = TicketsState(),
    onEvent: (TicketsEvent) -> Unit = {},
    onNavigationEvent: (TicketsNavigationEvents) -> Unit = {}
) {
    CheeseTopBarWrapper(topBarContent = {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(modifier = Modifier
                        .padding(start = CheeseTheme.paddings.smallest)
                        .width(CheeseTheme.paddings.large), onClick = {
                        onNavigationEvent(TicketsNavigationEvents.NavigateBack)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.back),
                        style = CheeseTheme.typography.common16Medium
                    )
                }
            }
            HorizontalDivider()
        }
    }) {
        AnimatedContent(
            targetState = state.loading, transitionSpec = {
                fadeIn().togetherWith(fadeOut())
            }, label = "Tickets loading"
        ) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                TicketsScreenContent(
                    state = state,
                    onEvent = onEvent,
                    onNavigationEvent = onNavigationEvent
                )
            }
        }
    }
}

@Composable
private fun TicketsScreenContent(
    state: TicketsState,
    onEvent: (TicketsEvent) -> Unit,
    onNavigationEvent: (TicketsNavigationEvents) -> Unit
) {
    LazyColumn {
        item {
            Text(
                modifier = Modifier.padding(CheeseTheme.paddings.medium),
                text = stringResource(R.string.my_tickets),
                style = CheeseTheme.typography.common24Bold
            )
        }
        state.ticketsOfActivities?.let { tickets ->
            items(
                items = tickets,
                key = { it.id }
            ) { item ->
                
            }
        }
    }
}

@Composable
private fun TicketView(
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