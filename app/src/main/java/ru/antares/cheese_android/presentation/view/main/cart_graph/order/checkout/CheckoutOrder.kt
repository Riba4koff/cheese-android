@file:OptIn(ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.cart_graph.order.checkout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.AppError
import ru.antares.cheese_android.domain.models.CartProductModel
import ru.antares.cheese_android.domain.models.CategoryModel
import ru.antares.cheese_android.domain.models.ProductModel
import ru.antares.cheese_android.domain.paymentType.PaymentType
import ru.antares.cheese_android.presentation.components.buttons.CheeseButton
import ru.antares.cheese_android.presentation.components.shaker.ShakeConfig
import ru.antares.cheese_android.presentation.components.shaker.ShakeController
import ru.antares.cheese_android.presentation.components.shaker.rememberShakeController
import ru.antares.cheese_android.presentation.components.shaker.shake
import ru.antares.cheese_android.presentation.components.textfields.CheeseTextField
import ru.antares.cheese_android.presentation.models.AddressModel
import ru.antares.cheese_android.presentation.models.ProductUIModel
import ru.antares.cheese_android.presentation.util.parsePrice
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.vibrate
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 18.03.2024 at 13:36
 * Android Studio
 */

@Preview
@Composable
fun CheckoutOrderScreenPreview() {
    CheeseTheme {
        CheckoutOrderScreen(
            state = CheckoutOrderState(

            ),
            onEvent = {

            },
            onNavigationEvent = {

            },
            onError = {

            },
            totalCost = 0.0
        )
    }
}

@Preview
@Composable
fun PaymentTypePreview() {
    CheeseTheme {
        PaymentMethodItem(
            paymentType = PaymentType.CardToCourier(),
            onClick = {

            }
        )
    }
}

@Preview
@Composable
fun CheckoutOrderPreview() {
    CheeseTheme {
        OrderProductView(
            cartProductModel = CartProductModel(
                amount = 1,
                price = 0.0,
                priceWithDiscount = null,
                product = ProductModel(
                    id = "1",
                    name = "Test",
                    description = "Test",
                    price = 0.0,
                    category = CategoryModel(
                        id = "1",
                        name = "Test"
                    ),
                    unit = 0,
                    unitName = "Test",
                    categoryId = "1",
                    categories = emptyList(),
                    recommend = false,
                    outOfStock = false
                )
            )
        )
    }
}

@Composable
fun CheckoutOrderScreen(
    totalCost: Double,
    state: CheckoutOrderState,
    onEvent: (CheckoutOrderEvent) -> Unit,
    onNavigationEvent: (CheckoutOrderNavigationEvent) -> Unit,
    onError: (AppError) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.back),
                        style = CheeseTheme.typography.common18Regular
                    )
                }, navigationIcon = {
                    IconButton(
                        modifier = Modifier.size(32.dp),
                        onClick = {
                            onNavigationEvent(CheckoutOrderNavigationEvent.NavigateBack)
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                }, scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            CreateOrderScreenContent(
                state = state,
                onEvent = onEvent,
                onNavigationEvent = onNavigationEvent,
                totalCost = totalCost
            )
        }
    }
}

@Composable
private fun CreateOrderScreenContent(
    totalCost: Double,
    state: CheckoutOrderState,
    onEvent: (CheckoutOrderEvent) -> Unit,
    onNavigationEvent: (CheckoutOrderNavigationEvent) -> Unit
) {
    val addressShakeController = rememberShakeController()
    val paymentShakeController = rememberShakeController()
    val shakeConfig = ShakeConfig(
        iterations = 10,
        translateX = -2f
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(
                horizontal = CheeseTheme.paddings.medium,
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
    ) {
        Spacer(modifier = Modifier)
        Address(
            shakeController = addressShakeController,
            address = state.address,
            editAddress = {
                onNavigationEvent(CheckoutOrderNavigationEvent.NavigateToSelectAddress)
            }
        )
        Receiver(
            receiver = state.receiver,
            onReceiverChange = { receiver ->
                onEvent(CheckoutOrderEvent.OnReceiverChange(receiver))
            }
        )
        CheckoutOrderProducts(
            products = state.products
        )
        PaymentMethod(
            shakeController = paymentShakeController,
            paymentType = state.paymentMethod,
            onPaymentTypeChange = { paymentType ->
                onEvent(CheckoutOrderEvent.OnPaymentMethodChange(paymentType))
            }
        )
        Comment(
            comment = state.comment,
            onCommentChange = { comment ->
                onEvent(CheckoutOrderEvent.OnCommentChange(comment))
            }
        )
        Spacer(modifier = Modifier.height(128.dp))
    }
    Box(modifier = Modifier.fillMaxSize()) {
        CreateOrderButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(CheeseTheme.paddings.medium),
            totalCost = totalCost,
            onClick = {
                if (state.address == null) {
                    scope.launch {
                        vibrate(context)
                        addressShakeController.shake(shakeConfig)
                    }
                } else if (state.paymentMethod == null) {
                    scope.launch {
                        vibrate(context)
                        paymentShakeController.shake(shakeConfig)
                    }
                } else {
                    onNavigationEvent(
                        CheckoutOrderNavigationEvent.NavigateToConfirmOrder(
                            address = state.address,
                            receiver = state.receiver,
                            paymentMethod = state.paymentMethod,
                            comment = state.comment
                        )
                    )
                }
            }
        )
    }
}

@Composable
private fun Address(
    shakeController: ShakeController,
    address: AddressModel?,
    editAddress: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SelectAddressTitle(editAddress)
        SelectedAddress(address, shakeController)
    }
}

@Composable
private fun SelectAddressTitle(
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressedAnimation by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        label = "pressed animation",
        animationSpec = tween(100)
    )
    val pressedOpacity by animateFloatAsState(
        targetValue = if (pressed) 0.5f else 1f,
        label = "pressed animation",
        animationSpec = tween(100)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.delivery_address),
            style = CheeseTheme.typography.common16Semibold
        )
        Icon(modifier = Modifier
            .size(18.dp)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                }, onTap = {
                    onClick()
                })
            }
            .scale(pressedAnimation)
            .alpha(pressedOpacity),
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            tint = CheeseTheme.colors.accent)
    }
}

@Composable
private fun SelectedAddress(
    address: AddressModel?,
    shakeController: ShakeController
) {
    address?.get()?.let {
        Text(
            modifier = Modifier
                .padding(vertical = CheeseTheme.paddings.small),
            text = it,
            style = CheeseTheme.typography.common12Regular,
            color = CheeseTheme.colors.darkGray
        )
    } ?: Text(
        modifier = Modifier
            .padding(vertical = CheeseTheme.paddings.small)
            .shake(shakeController = shakeController),
        text = "Выберите адрес доставки",
        style = CheeseTheme.typography.common12Regular,
        color = CheeseTheme.colors.darkGray
    )
}

@Composable
private fun Receiver(
    receiver: String,
    onReceiverChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.receiver),
            style = CheeseTheme.typography.common16Semibold
        )
        CheeseTextField(
            value = receiver,
            onValueChange = onReceiverChange,
            placeholder = stringResource(R.string.input_credentials_of_receiver),
            enabled = true
        )
    }
}

@Composable
private fun PaymentMethod(
    shakeController: ShakeController,
    paymentType: PaymentType?,
    onPaymentTypeChange: (PaymentType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.receiver),
            style = CheeseTheme.typography.common16Semibold
        )
        PaymentMethodItem(
            modifier = Modifier
                .shake(shakeController),
            paymentType = PaymentType.Cash(),
            onClick = { type ->
                onPaymentTypeChange(type)
            },
            backgroundColor = if (paymentType?.name == PaymentType.Cash().name) CheeseTheme.colors.accent
            else CheeseTheme.colors.gray.copy(0.1f)
        )
        PaymentMethodItem(
            modifier = Modifier
                .shake(shakeController),
            paymentType = PaymentType.CardToCourier(),
            onClick = { type ->
                onPaymentTypeChange(type)
            },
            backgroundColor = if (paymentType?.name == PaymentType.CardToCourier().name) CheeseTheme.colors.accent
            else CheeseTheme.colors.gray.copy(0.1f)
        )
    }
}

@Composable
private fun PaymentMethodItem(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = CheeseTheme.shapes.medium,
    backgroundColor: Color = CheeseTheme.colors.accent,
    paymentType: PaymentType,
    onClick: (PaymentType) -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val pressedAnimation by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f, label = "pressed animation"
    )
    val pressedOpacity by animateFloatAsState(
        targetValue = if (pressed) 0.6f else 1f,
        label = "pressed animation",
        animationSpec = tween(50)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                }, onTap = {
                    onClick(paymentType)
                })
            }
            .scale(pressedAnimation)
            .alpha(pressedOpacity)
            .background(color = backgroundColor, shape),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(CheeseTheme.paddings.medium)
        ) {
            Icon(painter = painterResource(id = paymentType.icon), contentDescription = null)
            Text(
                modifier = modifier
                    .padding(
                        horizontal = CheeseTheme.paddings.medium,
                        vertical = CheeseTheme.paddings.small
                    ),
                text = paymentType.value
            )
        }
    }
}

@Composable
private fun Comment(
    comment: String,
    onCommentChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.comment_not_necessary),
            style = CheeseTheme.typography.common16Semibold
        )
        CheeseTextField(
            value = comment,
            onValueChange = onCommentChange,
            placeholder = stringResource(R.string.comment),
            enabled = true
        )
    }
}

@Composable
private fun CreateOrderButton(
    modifier: Modifier = Modifier,
    totalCost: Double,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Row(
            modifier = Modifier
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
                text = "${parsePrice(totalCost)}₽",
                style = CheeseTheme.typography.common16Semibold
            )
        }
        CheeseButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            text = stringResource(R.string.checkout_order)
        ) {
            onClick()
        }
    }
}

@Composable
private fun CheckoutOrderProducts(
    products: List<CartProductModel>
) {
    var listIsOpen by remember {
        mutableStateOf(false)
    }
    val sizeOfList = if (listIsOpen) products.size else 2

    Column(
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.products_in_order),
            style = CheeseTheme.typography.common16Semibold
        )
        products.forEachIndexed { index, product ->
            if (index <= sizeOfList - 1) {
                OrderProductView(
                    cartProductModel = product
                )
            }
        }
        if (products.size > 2) {
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(32.dp),
                onClick = {
                    listIsOpen = !listIsOpen
                }
            ) {
                if (!listIsOpen) Text(
                    text = stringResource(R.string.show_more),
                    color = CheeseTheme.colors.blue
                )
                else Text(
                    text = stringResource(R.string.hide),
                    color = CheeseTheme.colors.blue
                )
            }
        }
    }
}

@Composable
fun OrderProductView(
    modifier: Modifier = Modifier,
    cartProductModel: CartProductModel,
    backgroundColor: Color = CheeseTheme.colors.white,
    shape: RoundedCornerShape = CheeseTheme.shapes.medium
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape)
            .border(1.dp, CheeseTheme.colors.lightGray, shape)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .width(140.dp)
                    .height(90.dp)
                    .padding(CheeseTheme.paddings.medium)
                    .clip(CheeseTheme.shapes.small),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cartProductModel.product.imageUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.product_place_holder)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = CheeseTheme.paddings.medium),
                    verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
                ) {
                    Text(
                        text = cartProductModel.product.name,
                        style = CheeseTheme.typography.common16Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = cartProductModel.product.name,
                        style = CheeseTheme.typography.common14Regular,
                        color = CheeseTheme.colors.gray
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(end = CheeseTheme.paddings.medium),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${parsePrice(cartProductModel.product.price * cartProductModel.amount)}₽",
                        style = CheeseTheme.typography.common16Medium
                    )
                    Text(
                        text = "${cartProductModel.product.unit * cartProductModel.amount}" + " ${cartProductModel.product.unitName}",
                        style = CheeseTheme.typography.common14Regular,
                        color = CheeseTheme.colors.gray
                    )
                }
            }
        }
    }
}