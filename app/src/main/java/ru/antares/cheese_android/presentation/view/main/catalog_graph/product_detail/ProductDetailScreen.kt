@file:OptIn(ExperimentalFoundationApi::class)

package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.ProductModel
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.presentation.components.screens.ErrorScreen
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.models.CategoryUIModel
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.util.parsePrice
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.LoadingProductView
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductView
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * ProductDetailScreen.kt
 * @author Павел
 * Created by on 21.02.2024 at 22:01
 * Android studio
 */

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    CheeseTheme {
        ProductDetailScreen(
            state = ProductDetailViewState(
                loading = false,
                product = ProductUIModel(
                    value = ProductModel(
                        id = "b217457a-da4a-40c9-a20c-4872d6281ca4",
                        name = "Сырная тарелка Любитель",
                        description = "Тарелка для вдумчивого изучения сыров от Месье Рокфор. Составляется ПОД ЗАКАЗ из изысканных образцов, имеющихся в наличии. Пожелания обговариваются индивидуально" +
                                "Тарелка для вдумчивого изучения сыров от Месье Рокфор. Составляется ПОД ЗАКАЗ из изысканных образцов, имеющихся в наличии. Пожелания обговариваются индивидуально" +
                                "Тарелка для вдумчивого изучения сыров от Месье Рокфор. Составляется ПОД ЗАКАЗ из изысканных образцов, имеющихся в наличии. Пожелания обговариваются индивидуально",
                        unit = 0,
                        category = CategoryUIModel(
                            id = "",
                            name = "Сыр",
                            position = 0,
                            parentID = null
                        ),
                        price = 15000.0,
                        categories = emptyList(),
                        categoryId = "",
                        recommend = false,
                        outOfStock = false,
                        unitName = "шт"
                    ),
                    countInCart = 0
                ),
                uiError = null,
                recommendations = listOf(
                    ProductUIModel(
                        value = ProductModel(
                            id = "",
                            name = "Сыр твердый очень вкусный",
                            price = 15000.0,
                            description = "",
                            unit = 0,
                            category = CategoryUIModel(
                                id = "",
                                name = "Сыр",
                                position = 0,
                                parentID = null
                            ),
                            categories = emptyList(),
                            categoryId = "",
                            recommend = false,
                            outOfStock = false,
                            unitName = "шт"
                        ),
                        countInCart = 0
                    ),
                    ProductUIModel(
                        value = ProductModel(
                            id = "123",
                            name = "Сыр твердый очень вкусный",
                            price = 15000.0,
                            description = "",
                            unit = 0,
                            category = CategoryUIModel(
                                id = "",
                                name = "Сыр",
                                position = 0,
                                parentID = null
                            ),
                            categories = emptyList(),
                            categoryId = "",
                            recommend = false,
                            outOfStock = false,
                            unitName = "шт"
                        ),
                        countInCart = 502
                    )
                ),
                loadingNextPageRecommendations = true
            ),
            onEvent = {

            },
            onError = {

            },
            onNavigationEvent = {

            }
        )
    }
}

@Composable
fun ProductDetailScreen(
    state: ProductDetailViewState,
    onEvent: (ProductDetailEvent) -> Unit,
    onNavigationEvent: (ProductDetailNavigationEvent) -> Unit,
    onError: (UIError) -> Unit
) {
    CheeseTopBarWrapper(
        topBarContent = {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = CheeseTheme.paddings.smallest)
                        .size(CheeseTheme.paddings.large),
                    onClick = {
                        onNavigationEvent(ProductDetailNavigationEvent.GoBack)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(id = R.string.back),
                    style = CheeseTheme.typography.common16Regular
                )
            }
        }
    ) {
        AnimatedContent(
            targetState = state.loading,
            label = "Product detail animated content"
        ) { isLoading ->
            if (isLoading) {
                LoadingScreen()
            } else {
                ProductDetailScreenContent(
                    state = state,
                    onEvent = onEvent,
                    onNavigationEvent = onNavigationEvent
                )
            }
        }

        state.uiError?.let { uiError ->
            ErrorScreen(error = uiError, onError = onError)
        }
    }
}

@Composable
fun ProductDetailScreenContent(
    state: ProductDetailViewState,
    onEvent: (ProductDetailEvent) -> Unit,
    onNavigationEvent: (ProductDetailNavigationEvent) -> Unit
) {
    state.product?.let { product ->
        val filteredRecommendations = remember { mutableStateOf(state.recommendations) }

        LaunchedEffect(key1 = state.recommendations) {
            filteredRecommendations.value = state.recommendations.filter { recommendation ->
                recommendation.value.id != product.value.id
            }
        }

        LaunchedEffect(key1 = state.recommendations.isEmpty()) {
            Log.d("loading_recommendations", "top")
            if (state.recommendations.isEmpty()) {
                Log.d("loading_recommendations", "bottom")
                onEvent(
                    ProductDetailEvent.LoadNextPageOfRecommendations(
                        categoryID = product.value.categoryId,
                        page = 0,
                        size = 4
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            item {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(246.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.value.imageUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.product_place_holder)
                        .build(),
                    contentDescription = "product image",
                    contentScale = ContentScale.Crop
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = CheeseTheme.paddings.medium)
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = product.value.name, style = CheeseTheme.typography.common28Bold)

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${parsePrice(product.value.price)}₽",
                        style = CheeseTheme.typography.common16Semibold
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${product.value.unit} ${product.value.unitName}",
                        style = CheeseTheme.typography.common14Regular
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.description),
                        style = CheeseTheme.typography.common12Medium,
                        color = CheeseTheme.colors.gray
                    )
                    ProductDescription(product.value.description)
                }
            }
            item {
                Text(
                    modifier = Modifier
                        .padding(horizontal = CheeseTheme.paddings.medium)
                        .padding(top = CheeseTheme.paddings.large, bottom = CheeseTheme.paddings.medium),
                    text = stringResource(R.string.products_recommendations),
                    style = CheeseTheme.typography.common28Bold
                )
            }
            itemsIndexed(
                items = filteredRecommendations.value,
                key = { _, it -> it.value.id }
            ) { index, product ->
                ProductView(
                    modifier = Modifier
                        .animateItemPlacement(tween(50))
                        .padding(horizontal = CheeseTheme.paddings.medium)
                        .padding(bottom = CheeseTheme.paddings.small),
                    product = product,
                    onClick = { recommendation ->
                        onNavigationEvent(
                            ProductDetailNavigationEvent.NavigateToProduct(
                                recommendation.value.id
                            )
                        )
                    }
                )
                if (index >= state.recommendations.size - 1 && !state.loadingNextPageRecommendations && !state.endReached) onEvent(
                    ProductDetailEvent.LoadNextPageOfRecommendations(
                        categoryID = product.value.categoryId,
                        page = state.currentPage + 1,
                        size = state.pageSize
                    )
                )
            }
            item {
                if (state.loadingNextPageRecommendations && !state.endReached) {
                    LoadingProductView(
                        modifier = Modifier
                            .animateItemPlacement(tween(50))
                            .padding(horizontal = CheeseTheme.paddings.medium)
                            .padding(bottom = CheeseTheme.paddings.small)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDescription(description: String) {
    val (descriptionExpanded, onDescriptionExpandedChange) = remember {
        mutableStateOf(false)
    }

    Spacer(modifier = Modifier.height(4.dp))
    Text(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onDescriptionExpandedChange(descriptionExpanded.not())
                }
            ),
        text = description,
        style = CheeseTheme.typography.common14Regular,
        maxLines = if (descriptionExpanded) Int.MAX_VALUE else 5,
        overflow = TextOverflow.Ellipsis
    )
}