@file:OptIn(ExperimentalFoundationApi::class)

package ru.antares.cheese_android.presentation.view.main.community_graph.activity

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.domain.models.community.ActivityModel
import ru.antares.cheese_android.domain.models.community.EventModel
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.util.parsePrice
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductView
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * ActivityScreen.kt
 * @author Павел
 * Created by on 06.04.2024 at 20:03
 * Android studio
 */

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    CheeseTheme {
        ActivityScreen(
            state = ActivityScreenState(
                post = PostModel(
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
                            description = "Сырная тусовка - это уникальное мероприятие, сочетающее в себе дегустацию сыра и атмосферу уютной культурной рюмочной. Вечером, под приятную музыку и свечи, участники смогут попробовать разнообразные сорта сыра и насладиться общением в уютной обстановке.",
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
                products = (1..3).map {
                    ProductUIModel(
                        value = ProductModel(
                            id = "$it",
                            name = "Сыр твердый очень вкусный",
                            price = 15000.0,
                            description = "",
                            unit = 0,
                            category = CategoryModel(
                                id = "", name = "Сыр", position = 0, parentID = null
                            ),
                            categories = emptyList(),
                            categoryId = "",
                            recommend = false,
                            outOfStock = false,
                            unitName = "шт"
                        ), countInCart = 0
                    )
                }
            ),
            onEvent = {},
            onNavigationEvent = {}
        )
    }
}

@Composable
fun ActivityScreen(
    state: ActivityScreenState,
    onEvent: (ActivityEvent) -> Unit,
    onNavigationEvent: (ActivityNavigationEvent) -> Unit
) {
    CheeseTopBarWrapper(
        topBarContent = {
            Row(
                Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = CheeseTheme.paddings.smallest)
                        .size(CheeseTheme.paddings.large), onClick = {
                        onNavigationEvent(ActivityNavigationEvent.NavigateBack)
                    }
                ) {
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
    ) {
        AnimatedContent(
            targetState = state.loading,
            label = "Community detail screen animated content",
            transitionSpec = {
                fadeIn(tween(200)).togetherWith(fadeOut(tween(200)))
            }
        ) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                ActivityScreenContent(
                    state = state,
                    onEvent = onEvent,
                    onNavigationEvent = onNavigationEvent
                )
            }
        }
    }
}

@Composable
private fun ActivityScreenContent(
    state: ActivityScreenState,
    onEvent: (ActivityEvent) -> Unit,
    onNavigationEvent: (ActivityNavigationEvent) -> Unit
) {
    state.post?.let { post ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(252.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.imageURL)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(CheeseTheme.paddings.medium)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
                ) {
                    post.activityModel?.let { activity ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
                        ) {
                            Text(
                                text = activity.event.title,
                                style = CheeseTheme.typography.common30Bold
                            )
                            ActivityPrice(activity)
                        }
                        AddressWithDateActivity(activity)
                        ActivityDescription(activity)
                    }
                }
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = CheeseTheme.paddings.medium),
                    text = "Что будем дегустировать?",
                    style = CheeseTheme.typography.common20Semibold
                )
            }
            items(state.products) { product ->
                ProductView(
                    modifier = Modifier
                        .padding(horizontal = CheeseTheme.paddings.medium)
                        .padding(top = CheeseTheme.paddings.medium)
                        .animateItemPlacement(tween(50)),
                    product = product,
                    onClick = { model ->
                        onNavigationEvent(ActivityNavigationEvent.NavigateToProduct(model.value.id))
                    },
                    addToCart = { model ->
                        onEvent(
                            ActivityEvent.AddProductToCart(
                                model.value.id,
                                model.countInCart
                            )
                        )
                    },
                    removeFromCart = { model ->
                        onEvent(
                            ActivityEvent.RemoveProductFromCart(
                                model.value.id,
                                model.countInCart
                            )
                        )
                    }
                )
            }
        }
    }
    AnimatedVisibility(
        visible = state.loadingCart,
        enter = fadeIn(tween(64)),
        exit = fadeOut(tween(64))
    ) {
        LoadingScreen(modifier = Modifier.clickable { /*DO NOTHING*/ })
    }
}

@Composable
private fun ActivityDescription(activity: ActivityModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
    ) {
        Text(
            text = stringResource(id = R.string.description),
            style = CheeseTheme.typography.common12Medium,
            color = Color.LightGray
        )
        Text(
            text = activity.event.description,
            style = CheeseTheme.typography.common14Medium
        )
    }
}

@Composable
private fun ActivityPrice(activity: ActivityModel) {
    Text(
        text = "Цена билета: ${parsePrice(activity.ticketPrice)}₽",
        style = CheeseTheme.typography.common20Semibold
    )
}

@Composable
fun AddressWithDateActivity(activity: ActivityModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null
            )
            Text(
                text = activity.address,
                style = CheeseTheme.typography.common14Medium
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null
            )
            Text(
                text = activity.startFrom,
                style = CheeseTheme.typography.common14Medium
            )
        }
    }
}