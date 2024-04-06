package ru.antares.cheese_android.presentation.view.main.community_graph.post

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ru.antares.cheese_android.domain.models.community.PostModel
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductView
import ru.antares.cheese_android.presentation.view.main.community_graph.activity.ActivityNavigationEvent
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * PostScreen.kt
 * @author Павел
 * Created by on 06.04.2024 at 23:41
 * Android studio
 */

@Preview(showBackground = true)
@Composable
fun PostScreenPreview() {
    CheeseTheme {
        PostScreen(
            state = PostScreenState(
                loading = false,
                post = PostModel(
                    id = "1",
                    title = "Вкусные завтраки с сыром",
                    subtitle = "Греческий омлет с фетой",
                    description = "Сырная тусовка - это уникальное мероприятие, сочетающее в себе дегустацию сыра и атмосферу уютной культурной рюмочной. Вечером, под приятную музыку и свечи, участники смогут попробовать разнообразные сорта сыра и насладиться общением в уютной обстановке.",
                    createdAt = "createdAt",
                    publishedAt = "publishedAt",
                    activityModel = null,
                    products = emptyList(),
                    posts = listOf(),
                ),
                products = listOf(
                    ProductUIModel(
                        value = ProductModel(
                            id = "1",
                            name = "title",
                            description = "description",
                            price = 1000.0,
                            unit = 0,
                            category = CategoryModel(
                                id = "1",
                                name = "title",
                                position = 0,
                                parentID = null
                            ),
                            categoryId = "",
                            categories = emptyList(),
                            recommend = false,
                            outOfStock = false,
                            unitName = ""
                        ),
                        countInCart = 0,
                        isFavorite = false
                    ),
                    ProductUIModel(
                        value = ProductModel(
                            id = "2",
                            name = "title",
                            description = "description",
                            price = 1000.0,
                            unit = 0,
                            category = CategoryModel(
                                id = "1",
                                name = "title",
                                position = 0,
                                parentID = null
                            ),
                            categoryId = "",
                            categories = emptyList(),
                            recommend = false,
                            outOfStock = false,
                            unitName = ""
                        ),
                        countInCart = 0,
                        isFavorite = false
                    )
                ),
            ),
            onEvent = {},
            onNavigationEvent = {

            }
        )
    }
}

@Composable
fun PostScreen(
    state: PostScreenState,
    onEvent: (PostEvent) -> Unit,
    onNavigationEvent: (PostNavigationEvent) -> Unit
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
                        onNavigationEvent(PostNavigationEvent.NavigateBack)
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
                PostScreenContent(
                    state = state,
                    onEvent = onEvent,
                    onNavigationEvent = onNavigationEvent
                )
            }
        }
    }
}

@Composable
private fun PostScreenContent(
    state: PostScreenState,
    onEvent: (PostEvent) -> Unit,
    onNavigationEvent: (PostNavigationEvent) -> Unit
) {
    state.post?.let { post ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
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
                        .padding(CheeseTheme.paddings.medium),
                    verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
                ) {
                    /** TITLE */
                    Text(text = post.title, style = CheeseTheme.typography.common30Bold)

                    /** SUBTITLE */
                    if (post.subtitle.isNotEmpty()) Text(
                        text = post.subtitle,
                        style = CheeseTheme.typography.common20Semibold
                    )
                    /** DESCRIPTION */
                    Text(
                        text = post.description,
                        style = CheeseTheme.typography.common14Regular
                    )

                    /** TITLE OF PRODUCTS */
                    Text(
                        text = stringResource(R.string.products),
                        style = CheeseTheme.typography.common20Semibold
                    )
                }
            }
            items(state.products, key = { it.value.id }) { item ->
                ProductView(
                    modifier = Modifier
                        .padding(horizontal = CheeseTheme.paddings.medium)
                        .padding(bottom = CheeseTheme.paddings.medium),
                    product = item,
                    onClick = { product ->
                        onNavigationEvent(PostNavigationEvent.NavigateToProduct(product.value.id))
                    },
                    addToCart = { product ->
                        onEvent(
                            PostEvent.AddProductToCart(
                                product.value.id,
                                product.countInCart
                            )
                        )
                    },
                    removeFromCart = { product ->
                        onEvent(
                            PostEvent.RemoveProductFromCart(
                                product.value.id,
                                product.countInCart
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