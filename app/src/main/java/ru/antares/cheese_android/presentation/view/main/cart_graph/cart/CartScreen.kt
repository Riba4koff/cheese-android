package ru.antares.cheese_android.presentation.view.main.cart_graph.cart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.antares.cheese_android.R
import ru.antares.cheese_android.clickable
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.presentation.components.ErrorAlertDialog
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.presentation.components.buttons.CheeseButton
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.shaker.ShakeConfig
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTitleWrapper
import ru.antares.cheese_android.presentation.util.parsePrice
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CheeseTheme {
        CartScreen(
            state = CartState(
                loading = false,
                products = (1..5).map {
                    CartProductModel(
                        amount = it,
                        price = it * 100.0,
                        priceWithDiscount = null,
                        product = ProductModel(
                            id = "$it",
                            name = "Сыр мастард мастардович",
                            price = 500.0,
                            description = "",
                            unit = 100,
                            category = CategoryModel(
                                id = "0",
                                name = "Сыры",
                                position = 0,
                                parentID = null
                            ),
                            categoryId = "0",
                            categories = emptyList(),
                            recommend = false,
                            outOfStock = false,
                            unitName = "гр"
                        )
                    )
                },
                error = null,
                totalCost = 12500.0
            ),
            onEvent = {

            },
            onNavigationEvent = {

            },
            onError = {

            }
        )
    }
}

@Composable
fun CartScreen(
    state: CartState,
    onEvent: (CartEvent) -> Unit,
    onNavigationEvent: (CartNavigationEvent) -> Unit,
    onError: (UIError) -> Unit
) {
    val error: MutableState<UIError?> = remember { mutableStateOf(null) }

    LaunchedEffect(state.error) {
        error.value = state.error
    }

    CheeseTitleWrapper(title = stringResource(R.string.cart_title)) {
        AnimatedContent(
            targetState = state.loading,
            label = "Cart animated content",
            transitionSpec = {
                fadeIn(tween(200)).togetherWith(fadeOut(tween(200)))
            }
        ) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                CartContent(
                    state = state,
                    onEvent = onEvent,
                    onNavigationEvent = onNavigationEvent
                )
            }
        }
    }

    error.value?.let {
        ErrorAlertDialog(error = it) {
            onError(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CartContent(
    state: CartState,
    onEvent: (CartEvent) -> Unit,
    onNavigationEvent: (CartNavigationEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                horizontal = CheeseTheme.paddings.medium,
            ),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
        ) {
            itemsIndexed(
                items = state.products,
                key = { _, it -> it.product.id }
            ) { index, product ->
                CartProductView(
                    modifier = Modifier
                        .animateItemPlacement(),
                    product = product, addToCart = { cpm ->
                        onEvent(CartEvent.AddProductToCart(cpm.product))
                    }, removeFromCart = { cpm ->
                        onEvent(CartEvent.RemoveProductFromCart(cpm.product))
                    }, deleteFromCart = { cpm ->
                        onEvent(CartEvent.DeleteProductFromCart(cpm.product))
                    }
                )
            }
            item {
                Spacer(Modifier.height(124.dp))
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = CheeseTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = CheeseTheme.paddings.medium)
                    .background(
                        color = CheeseTheme.colors.white,
                        shape = CheeseTheme.shapes.medium
                    )
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = CheeseTheme.colors.lightGray
                        ),
                        shape = CheeseTheme.shapes.medium
                    )
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = CheeseTheme.paddings.medium),
                    text = stringResource(R.string.total),
                    style = CheeseTheme.typography.common16Semibold
                )
                Text(
                    modifier = Modifier
                        .padding(end = CheeseTheme.paddings.medium),
                    text = "${parsePrice(state.totalCost)}₽",
                    style = CheeseTheme.typography.common16Semibold
                )
            }
            CheeseButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = CheeseTheme.paddings.medium),
                text = stringResource(R.string.go_checkout_order)
            ) {
                onNavigationEvent(CartNavigationEvent.ToCheckoutOrder)
            }
        }
    }
}

@Composable
private fun CartProductView(
    modifier: Modifier = Modifier,
    product: CartProductModel,
    addToCart: (CartProductModel) -> Unit,
    removeFromCart: (CartProductModel) -> Unit,
    deleteFromCart: (CartProductModel) -> Unit,
) {
    val priceText = buildAnnotatedString {
        val price = "${parsePrice(product.amount * product.price)}₽ "
        val unitAndUnitName = "${product.amount * product.product.unit}${product.product.unitName}"

        append(price)
        addStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ), start = 0, end = price.length
        )

        append(unitAndUnitName)
        addStyle(
            style = SpanStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = CheeseTheme.colors.gray
            ), start = price.length, end = price.length + unitAndUnitName.length
        )
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .aspectRatio(336f / 146f)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = CheeseTheme.colors.lightGray
                    ),
                    shape = CheeseTheme.shapes.medium
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = CheeseTheme.colors.accent,
                    strokeWidth = 2.dp
                )
                AsyncImage(
                    modifier = Modifier
                        .padding(CheeseTheme.paddings.medium)
                        .aspectRatio(1f / 1f)
                        .clip(CheeseTheme.shapes.small),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.product.imageUrl)
                        .crossfade(true)
                        .crossfade(200)
                        .build(),
                    contentDescription = "Async image of product",
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = CheeseTheme.paddings.medium)
                    .padding(end = CheeseTheme.paddings.medium)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = product.product.name,
                        style = CheeseTheme.typography.common16Regular,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    val (pressed, onPressedChange) = remember {
                        mutableStateOf(false)
                    }
                    val scaleDeleteIcon by animateFloatAsState(
                        targetValue = if (pressed) 0.95f else 1f,
                        label = "Delete icon animated scale"
                    )

                    Icon(
                        modifier = Modifier
                            .size(12.dp)
                            .clickable(
                                scale = scaleDeleteIcon,
                                onPressedChange = onPressedChange,
                                onClick = {
                                    deleteFromCart(product)
                                }
                            )
                            .scale(scaleDeleteIcon),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Delete product from cart"
                    )
                }
                Spacer(modifier = Modifier.height(CheeseTheme.paddings.smallest))
                Text(
                    text = product.product.category.name,
                    style = CheeseTheme.typography.common12Regular,
                    color = CheeseTheme.colors.gray
                )
                Spacer(modifier = Modifier.height(CheeseTheme.paddings.small))
                Text(
                    text = priceText,
                    color = CheeseTheme.colors.black
                )
                Spacer(modifier = Modifier.weight(1f))
                CartButtons(
                    addToCart = {
                        addToCart(product)
                    },
                    removeFromCart = {
                        removeFromCart(product)
                    },
                    amount = product.amount
                )
            }
        }
    }
}

@Composable
private fun CartButtons(
    amount: Int,
    removeFromCart: () -> Unit,
    addToCart: () -> Unit,
) {
    Row(
        modifier = Modifier
            .background(
                color = CheeseTheme.colors.accent,
                shape = CheeseTheme.shapes.medium
            )
            .height(40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.width(CheeseTheme.paddings.smallest))
        IconButton(modifier = Modifier.size(24.dp), onClick = removeFromCart) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.minus),
                contentDescription = null
            )
        }
        Text(text = "$amount")
        IconButton(modifier = Modifier.size(24.dp), onClick = addToCart) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.plus),
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(CheeseTheme.paddings.smallest))
    }
}