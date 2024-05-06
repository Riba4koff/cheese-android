@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package ru.antares.cheese_android.presentation.view.main.home_graph.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
import ru.antares.cheese_android.shimmerLoadingAnimation
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CheeseTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHorizontalPagerPreview() {
    CheeseTheme {
        HomeHorizontalPager(
            isLoading = true,
            title = "Блог",
            items = listOf("1", "2", "3", "4", "5")
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
fun HomeScreen() {
    val upcomingEventsTitle = "Ближайшие мероприятия"
    val recommendationsTitle = "Рекомендации"
    val blogTitle = "Блог"

    val activities = listOf(
        PostModel(
            id = "2",
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
        )
    )
    val posts = listOf(
        PostModel(
            id = "2",
            title = "Пост 1",
            description = "description",
            subtitle = "subtitle",
            createdAt = "createdAt",
            publishedAt = "publishedAt",
            activityModel = null,
            products = emptyList(),
            posts = emptyList(),
        ),
        PostModel(
            id = "2",
            title = "Новости",
            description = "description",
            subtitle = "subtitle",
            createdAt = "createdAt",
            publishedAt = "publishedAt",
            activityModel = null,
            products = emptyList(),
            posts = emptyList(),
        ),
        PostModel(
            id = "2",
            title = "Акция дня",
            description = "description",
            subtitle = "subtitle",
            createdAt = "createdAt",
            publishedAt = "publishedAt",
            activityModel = null,
            products = emptyList(),
            posts = emptyList(),
        ),
        PostModel(
            id = "2",
            title = "Самый вкусный сыр",
            description = "description",
            subtitle = "subtitle",
            createdAt = "createdAt",
            publishedAt = "publishedAt",
            activityModel = null,
            products = emptyList(),
            posts = emptyList(),
        )
    )
    val products = listOf(
        ProductUIModel(
            value = ProductModel(
                id = "",
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
        ),
        ProductUIModel(
            value = ProductModel(
                id = "",
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
        ),
        ProductUIModel(
            value = ProductModel(
                id = "",
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
    )

    val postsPagerState = rememberPagerState {
        activities.size
    }
    var loading1 by remember { mutableStateOf(true) }
    var loading2 by remember { mutableStateOf(true) }
    var loading3 by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            delay(2_000L)
            loading2 = false

            delay(1000L)
            loading1 = false

            delay(2000L)
            loading3 = false
        }
        scope.launch {
            while (true) {
                delay(5_000L)

                val nextPage =
                    if (postsPagerState.currentPage == postsPagerState.pageCount - 1) 0 else postsPagerState.currentPage + 1
                postsPagerState.animateScrollToPage(
                    nextPage,
                    animationSpec = spring(dampingRatio = 1.5f)
                )
            }
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
                isLoading = loading1,
                pagerState = postsPagerState,
                title = upcomingEventsTitle,
                items = posts
            ) { post ->
                PostItemView(post = post) {

                }
            }
            HomeHorizontalPager(
                isLoading = loading2,
                title = recommendationsTitle,
                items = activities
            ) { post ->
                post.activityModel?.let { activity ->
                    ActivityItemView(
                        modifier = Modifier,
                        postID = post.id,
                        model = activity,
                        imageUrl = post.imageURL,
                        onClickToPost = { postID: String ->

                        }
                    )
                }
            }
            HomeHorizontalPager(
                isLoading = loading3,
                title = blogTitle,
                items = products
            ) { product ->
                ProductView(
                    product = product,
                    onClick = { _ ->

                    }, addToCart = { _ ->

                    }, removeFromCart = { _ ->

                    }
                )
            }
        }
    }
}

@Composable
fun <T> HomeHorizontalPager(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    title: String,
    contentPadding: PaddingValues = PaddingValues(horizontal = CheeseTheme.paddings.medium),
    items: List<T>,
    pagerState: PagerState = rememberPagerState {
        items.size
    },
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
            Box(modifier = Modifier.aspectRatio(328f/ 223f)) {
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
                    item(items[page])
                }
            }
        }
    }
}