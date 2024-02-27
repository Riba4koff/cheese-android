@file:OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.antares.cheese_android.R
import ru.antares.cheese_android.clickable
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.onClick
import ru.antares.cheese_android.presentation.components.ErrorAlertDialog
import ru.antares.cheese_android.presentation.components.buttons.CheeseButton
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.util.parsePrice
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author: Павел
 * Date: 20.02.2024.
 */

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview() {
    CheeseTheme {
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
                    id = "123",
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
                ), countInCart = 502
            ),
        )
        ProductsScreen(name = "", state = ProductsState(
            products = products, loading = false, loadingCart = true
        ), onEvent = {

        }, onNavigationEvent = {

        }, onError = {

        })
    }
}

@Composable
fun ProductsScreen(
    name: String,
    state: ProductsState,
    onEvent: (ProductsEvent) -> Unit,
    onNavigationEvent: (ProductsNavigationEvent) -> Unit,
    onError: (UIError) -> Unit
) {
    val (search, onSearchChange) = remember { mutableStateOf("") }

    CheeseTopBarWrapper(topBarContent = {
        IconButton(modifier = Modifier
            .padding(start = CheeseTheme.paddings.smallest)
            .size(CheeseTheme.paddings.large)
            .align(Alignment.CenterStart), onClick = {
            onNavigationEvent(ProductsNavigationEvent.GoBack)
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = name,
            style = CheeseTheme.typography.common16Medium
        )
    }, enableClearButton = true, onSearchChange = onSearchChange, search = {
        /* TODO: navigate to search screen and make network call to receive products */
    }, searchValue = search
    ) {
        AnimatedContent(targetState = state.loading,
            label = "Products state animated content",
            transitionSpec = {
                fadeIn(tween(200)).togetherWith(fadeOut(tween(200)))
            }) { uiState ->
            when (uiState) {
                true -> {
                    LoadingScreen()
                }

                false -> {
                    if (state.products.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Тут пусто...",
                                style = CheeseTheme.typography.common12Regular,
                                color = CheeseTheme.colors.gray
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = CheeseTheme.paddings.medium,
                                vertical = CheeseTheme.paddings.small
                            ),
                            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
                        ) {
                            itemsIndexed(items = state.products,
                                key = { _, it -> it.value.id }) { index, product ->
                                ProductView(modifier = Modifier.animateItemPlacement(tween(50)),
                                    product = product,
                                    onClick = { pr ->
                                        onNavigationEvent(
                                            ProductsNavigationEvent.NavigateToProductDetailInfo(pr.value)
                                        )
                                    },
                                    addToCart = { pr ->
                                        onEvent(ProductsEvent.AddProductToCart(pr))
                                    },
                                    removeFromCart = { pr ->
                                        onEvent(ProductsEvent.RemoveProductFromCart(pr))
                                    })
                                if (index >= state.products.size - 1 && !state.loadingNextPage && !state.endReached) onEvent(
                                    ProductsEvent.LoadNextPage(
                                        page = state.currentPage + 1, size = state.pageSize
                                    )
                                )
                            }
                            item {
                                if (state.loadingNextPage && !state.endReached) {
                                    LoadingProductView(
                                        modifier = Modifier.animateItemPlacement(tween(50))
                                    )
                                }
                            }
                            item {

                            }
                        }
                    }
                }
            }
        }

        val error: MutableState<UIError?> = remember { mutableStateOf(null) }

        LaunchedEffect(state.error) {
            error.value = state.error
        }

        error.value?.let {
            ErrorAlertDialog(error = it) {
                error.value = null
                onError(it)
            }
        }
    }

    AnimatedVisibility(
        visible = state.loadingCart, enter = fadeIn(), exit = fadeOut()
    ) {
        LoadingScreen(
            modifier = Modifier
                .onClick { /* DO NOTHING */ }
        )
    }
}

@Composable
fun ProductView(
    modifier: Modifier = Modifier,
    product: ProductUIModel,
    onClick: (ProductUIModel) -> Unit,
    addToCart: (ProductUIModel) -> Unit,
    removeFromCart: (ProductUIModel) -> Unit,
) {
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val productAnimatedValue by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f, label = "Product view animated scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 2f)
            .clickable(scale = productAnimatedValue, onPressedChange = onPressedChange, onClick = {
                onClick(product)
            })
            .border(
                border = BorderStroke(0.5.dp, CheeseTheme.colors.gray.copy(0.3f)),
                shape = CheeseTheme.shapes.medium
            )
            .alpha(productAnimatedValue)
            .clip(CheeseTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier, contentAlignment = Alignment.Center) {
            androidx.compose.material.CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = CheeseTheme.colors.accent,
                strokeWidth = 2.dp
            )
            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
                model = ImageRequest.Builder(LocalContext.current).data(product.value.imageUrl)
                    .crossfade(true).build(),
                contentDescription = "product image",
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    CheeseTheme.colors.white.copy(0.8f), RoundedCornerShape(
                        topStart = CheeseTheme.paddings.medium,
                        topEnd = CheeseTheme.paddings.medium,
                    )
                )
                .padding(horizontal = CheeseTheme.paddings.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProductInfoView(
                modifier = Modifier.weight(1f), product = product
            )
            if (product.value.outOfStock.not()) {
                CartButtons(
                    product = product,
                    addToCart = addToCart,
                    removeFromCart = removeFromCart
                )
            }
        }
        ProductViewBlur(product)
    }
}

@Composable
fun ProductInfoView(
    modifier: Modifier = Modifier, product: ProductUIModel
) {
    val priceText = "${parsePrice(product.value.price)}₽"

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(
                end = CheeseTheme.paddings.small
            ),
            text = product.value.name,
            style = CheeseTheme.typography.common16Regular,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Text(
                text = priceText, style = CheeseTheme.typography.common14Bold
            )
            Text(
                text = "${product.value.unit} ${product.value.unitName}",
                style = CheeseTheme.typography.common14Regular,
                color = CheeseTheme.colors.black.copy(0.7f)
            )
        }
    }
}

@Composable
private fun ProductViewBlur(product: ProductUIModel) {
    val animatedBlur by animateDpAsState(
        targetValue = if (product.value.outOfStock && product.countInCart > 0) 4.dp else 0.dp,
        label = "Product blur animation"
    )

    Box(
        modifier = Modifier
            .aspectRatio(3f / 2f)
            .fillMaxWidth()
            .blur(animatedBlur),
        contentAlignment = Alignment.Center
    ) {
        if (product.value.outOfStock) {
            Text(
                text = stringResource(R.string.not_available),
                style = CheeseTheme.typography.common24Semibold,
                color = CheeseTheme.colors.white
            )
        }
    }
}

@Composable
fun LoadingProductView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 2f)
            .background(
                Color.LightGray.copy(0.1f), CheeseTheme.shapes.small
            ), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp), color = CheeseTheme.colors.accent, strokeWidth = 2.dp
        )
    }
}

@Composable
private fun CartButtons(
    product: ProductUIModel,
    addToCart: (ProductUIModel) -> Unit,
    removeFromCart: (ProductUIModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(CheeseTheme.shapes.medium)
            .background(
                CheeseTheme.colors.accent, CheeseTheme.shapes.medium
            )
            .height(40.dp)
            .clickable(interactionSource = remember { MutableInteractionSource() },
                indication = if (product.countInCart == 0) rememberRipple() else null,
                onClick = {
                    if (product.countInCart == 0) {
                        addToCart(product)
                    }
                })
    ) {
        if (product.countInCart == 0) Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = CheeseTheme.paddings.large),
            text = stringResource(R.string.buy),
            style = CheeseTheme.typography.common14Semibold
        ) else {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = CheeseTheme.paddings.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(modifier = Modifier.size(24.dp), onClick = {
                    removeFromCart(product)
                }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.minus),
                        contentDescription = null
                    )
                }
                Text(text = "${product.countInCart}")
                IconButton(modifier = Modifier.size(24.dp), onClick = {
                    addToCart(product)
                }) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = null
                    )
                }
            }
        }
    }
}