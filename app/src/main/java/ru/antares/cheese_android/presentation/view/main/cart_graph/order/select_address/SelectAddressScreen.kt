package ru.antares.cheese_android.presentation.view.main.cart_graph.order.select_address

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.buttons.CheeseButton
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.models.AddressModel
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.AddressLoadingView
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.AddressesEvent
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 20.05.2024 at 14:44
 * Android Studio
 */

@Preview(showBackground = true)
@Composable
private fun SelectAddressScreenPreview() {
    CheeseTheme {
        SelectAddressScreen(
            state = SelectAddressScreenState(
                addresses = listOf(
                    AddressModel(
                        id = "",
                        userID = "",
                        city = "Великий Новгород",
                        street = "Псковская",
                        house = "40",
                        building = "1",
                        title = "",
                        apartment = "208",
                        entrance = "6",
                        floor = "1"
                    )
                ),
                loading = false
            ),
            onEvent = {},
            onNavigationEvent = {}
        )
    }
}

@Composable
fun SelectAddressScreen(
    state: SelectAddressScreenState,
    onEvent: (SelectAddressEvent) -> Unit,
    onNavigationEvent: (SelectAddressNavigationEvent) -> Unit
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
                        onNavigationEvent(SelectAddressNavigationEvent.NavigateBack)
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
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                onClick = {
                    onNavigationEvent(SelectAddressNavigationEvent.NavigateToAddAddress)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.add),
                    color = CheeseTheme.colors.blue
                )
            }
        }
    ) {
        AnimatedContent(
            targetState = state.loading,
            label = "Loading addresses",
            transitionSpec = {
                fadeIn(tween(100)).togetherWith(fadeOut(tween(100)))
            }
        ) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                SelectAddressContent(
                    state = state,
                    onEvent = onEvent,
                    onNavigationEvent = onNavigationEvent
                )
            }
        }
    }
}

@Composable
private fun SelectAddressContent(
    state: SelectAddressScreenState,
    onEvent: (SelectAddressEvent) -> Unit,
    onNavigationEvent: (SelectAddressNavigationEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn{
            itemsIndexed(state.addresses, key = { _, it -> it.id }) { index, address->
                SelectAddressItem(
                    address = address,
                    onClick = onEvent,
                    isSelected = state.address?.id == address.id
                )
                if (index >= state.addresses.size - 1 && !state.loadingNextPage && !state.endReached) onEvent(
                    SelectAddressEvent.LoadNextPage(
                        page = state.currentPage + 1, size = 16
                    )
                )
            }
            item {
                AnimatedVisibility(
                    visible = !state.endReached && state.loadingNextPage,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    AddressLoadingView()
                }
            }
        }
        CheeseButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CheeseTheme.paddings.medium)
                .height(64.dp)
                .align(Alignment.BottomCenter), text = "Выбрать"
        ) {
            state.address?.id?.let { addressID ->
                onNavigationEvent(SelectAddressNavigationEvent.NavigateToCheckoutOrder(addressID))
            }
        }
    }
}

@Composable
fun SelectAddressItem(
    address: AddressModel,
    onClick: (SelectAddressEvent.SetAddress) -> Unit,
    isSelected: Boolean
) {
    val colors = RadioButtonDefaults.colors(
        selectedColor = CheeseTheme.colors.accent,
        unselectedColor = CheeseTheme.colors.accent
    )

    Row(
        modifier = Modifier.fillMaxWidth().clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(),
            onClick = {
                onClick(SelectAddressEvent.SetAddress(address))
            }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onClick(SelectAddressEvent.SetAddress(address))
            },
            colors = colors
        )
        Text(
            modifier = Modifier.padding(end = CheeseTheme.paddings.medium),
            text = address.get(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = CheeseTheme.typography.common12Regular
        )
    }
}