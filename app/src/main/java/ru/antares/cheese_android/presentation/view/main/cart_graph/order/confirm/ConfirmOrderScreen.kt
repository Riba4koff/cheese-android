@file:OptIn(ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.cart_graph.order.confirm

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.presentation.components.buttons.CheeseButton
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.models.AddressModel
import ru.antares.cheese_android.presentation.util.parsePrice
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout.CheckoutOrderNavigationEvent
import ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout.OrderProducts
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 29.03.2024 at 15:56
 * Android Studio
 */

@Preview
@Composable
fun ConfirmOrderScreenPreview() {
    CheeseTheme {
        ConfirmOrderScreen(
            onEvent = {},
            onNavigationEvent = {},
            state = ConfirmOrderState(
                address = AddressModel(
                    city = "Великий Новгород",
                    street = "проспект Мира",
                    house = "дом 44",
                    apartment = "квартира 46",
                    building = "1",
                    title = "",
                    userID = "",
                    id = ""
                ),
                receiver = "Иванов Иван Иванович",
                paymentMethod = PaymentType.CardToCourier(),
                comment = "Какой-то комментарий",
                totalCost = 12500.0,
                products = listOf(
                    CartProductModel(
                        amount = 1,
                        price = 12500.0,
                        priceWithDiscount = null,
                        product = ProductModel(
                            id = "1",
                            name = "Мягкий сыр ",
                            description = "Test",
                            price = 0.0,
                            category = CategoryModel(
                                id = "1",
                                name = "Мягкие"
                            ),
                            unit = 100,
                            unitName = "гр",
                            categoryId = "1",
                            categories = emptyList(),
                            recommend = false,
                            outOfStock = false
                        )
                    ),
                    CartProductModel(
                        amount = 1,
                        price = 12500.0,
                        priceWithDiscount = null,
                        product = ProductModel(
                            id = "2",
                            name = "Мягкий сыр ",
                            description = "Test",
                            price = 0.0,
                            category = CategoryModel(
                                id = "1",
                                name = "Мягкие"
                            ),
                            unit = 100,
                            unitName = "гр",
                            categoryId = "1",
                            categories = emptyList(),
                            recommend = false,
                            outOfStock = false
                        )
                    ),
                    CartProductModel(
                        amount = 1,
                        price = 12500.0,
                        priceWithDiscount = null,
                        product = ProductModel(
                            id = "3",
                            name = "Мягкий сыр ",
                            description = "Test",
                            price = 0.0,
                            category = CategoryModel(
                                id = "1",
                                name = "Мягкие"
                            ),
                            unit = 100,
                            unitName = "гр",
                            categoryId = "1",
                            categories = emptyList(),
                            recommend = false,
                            outOfStock = false
                        )
                    ),
                    CartProductModel(
                        amount = 1,
                        price = 12500.0,
                        priceWithDiscount = null,
                        product = ProductModel(
                            id = "4",
                            name = "Мягкий сыр ",
                            description = "Test",
                            price = 0.0,
                            category = CategoryModel(
                                id = "1",
                                name = "Мягкие"
                            ),
                            unit = 100,
                            unitName = "гр",
                            categoryId = "1",
                            categories = emptyList(),
                            recommend = false,
                            outOfStock = false
                        )
                    )
                )
            )
        )
    }
}

@Composable
fun ConfirmOrderScreen(
    onEvent: (ConfirmOrderEvent) -> Unit,
    onNavigationEvent: (ConfirmOrderNavigationEvent) -> Unit,
    state: ConfirmOrderState
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.back),
                        style = CheeseTheme.typography.common18Regular
                    )
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.size(32.dp),
                        onClick = {
                            onNavigationEvent(ConfirmOrderNavigationEvent.NavigateBack)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddings ->
        ConfirmOrderScreenContent(
            modifier = Modifier.padding(paddings),
            onEvent = onEvent,
            onNavigationEvent = onNavigationEvent,
            state = state
        )
    }
}

@Composable
private fun ConfirmOrderScreenContent(
    modifier: Modifier = Modifier,
    onEvent: (ConfirmOrderEvent) -> Unit,
    onNavigationEvent: (ConfirmOrderNavigationEvent) -> Unit,
    state: ConfirmOrderState
) {
    AnimatedContent(
        targetState = state.loading,
        label = "confirm order loading",
        transitionSpec = {
            fadeIn(tween(128)).togetherWith(fadeOut(tween(128)))
        }
    ) { loading ->
        if (loading) {
            LoadingScreen()
        } else {
            Box(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(CheeseTheme.paddings.medium),
                    verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
                ) {
                    Address(state.address)
                    Receiver(state.receiver)
                    OrderProducts(state.products)
                    PaymentMethod(state.paymentMethod)
                    Comment(state.comment)
                    TotalCost(state.totalCost)
                    Spacer(modifier = Modifier.height(64.dp))
                }
                CheeseButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(CheeseTheme.paddings.medium)
                        .height(64.dp)
                        .align(Alignment.BottomCenter),
                    text = stringResource(R.string.pay)
                ) {
                    onEvent(ConfirmOrderEvent.Pay)
                }
            }
        }
    }
}

@Composable
private fun Address(
    address: AddressModel?
) {
    address?.get()?.let {
        Column(
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Text(
                text = stringResource(R.string.delivery_address),
                style = CheeseTheme.typography.common16Semibold
            )
            Text(
                text = it,
                style = CheeseTheme.typography.common12Regular
            )
        }
    }
}

@Composable
private fun Receiver(
    receiver: String?
) {
    receiver?.let {
        Column(
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Text(
                text = stringResource(R.string.receiver),
                style = CheeseTheme.typography.common16Semibold
            )
            Text(
                text = it,
                style = CheeseTheme.typography.common12Regular
            )
        }
    }
}

@Composable
private fun PaymentMethod(
    method: PaymentType?
) {
    method?.let {
        Column(
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Text(
                text = stringResource(R.string.payment_method),
                style = CheeseTheme.typography.common16Semibold
            )
            Text(
                text = it.value,
                style = CheeseTheme.typography.common12Regular
            )
        }
    }
}

@Composable
private fun Comment(
    comment: String?
) {
    comment?.let {
        Column(
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
        ) {
            Text(
                text = stringResource(R.string.comment),
                style = CheeseTheme.typography.common16Semibold
            )
            Text(
                text = it,
                style = CheeseTheme.typography.common12Regular
            )
        }
    }
}

@Composable
private fun TotalCost(
    totalCost: Double?
) {
    totalCost?.let {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.total),
                style = CheeseTheme.typography.common16Semibold
            )
            Text(
                text = "${parsePrice(totalCost)}₽",
                style = CheeseTheme.typography.common16Medium
            )
        }
    }
}

