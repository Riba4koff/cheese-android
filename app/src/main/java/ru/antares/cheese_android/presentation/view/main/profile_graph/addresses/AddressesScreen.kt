@file:OptIn(ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.swipe.SwipeToDeleteContainer
import ru.antares.cheese_android.presentation.components.topbars.CheeseTopAppBar
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.models.AddressModel
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsEvent
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsNavigationEvent
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 16:20
 * Android Studio
 */

@Preview(showBackground = true)
@Composable
fun AddressesScreenPreview() {
    CheeseTheme {
        AddressesScreen(
            state = AddressesScreenState(
                addresses = (1..8).map {
                    AddressModel(
                        id = "$it",
                        city = "Великий Новгород",
                        street = "проспект Мира",
                        house = "17",
                        building = "3",
                        userID = "1",
                        title = "Home",
                        apartment = "квартира 15",
                        entrance = "подъезд 1",
                        floor = "1"
                    )
                },
                loadingNextPage = true,
                endReached = false
            ),
            onEvent = {},
            onNavigationEvent = {}
        )
    }
}

@Composable
fun AddressesScreen(
    state: AddressesScreenState,
    onEvent: (AddressesEvent) -> Unit,
    onNavigationEvent: (AddressesNavigationEvent) -> Unit
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
                        onNavigationEvent(AddressesNavigationEvent.NavigateBack)
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
                    onNavigationEvent(AddressesNavigationEvent.NavigateToAddAddress)
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
            transitionSpec = {
                fadeIn().togetherWith(fadeOut())
            },
            label = "Addresses loading"
        ) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                AddressesScreenContent(
                    state = state,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun AddressesScreenContent(
    state: AddressesScreenState,
    onEvent: (AddressesEvent) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(
                modifier = Modifier
                    .padding(CheeseTheme.paddings.medium),
                text = stringResource(R.string.saved_addresses),
                style = CheeseTheme.typography.common24Bold
            )
        }
        itemsIndexed(
            items = state.addresses,
            key = { _, it -> it.id }
        ) { index, address ->
            AddressView(
                address = address,
                onEvent = onEvent,
            )
            if (index >= state.addresses.size - 1 && !state.loadingNextPage && !state.endReached) onEvent(
                AddressesEvent.LoadNextPage(
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
}

@Composable
private fun AddressView(
    address: AddressModel,
    onEvent: (AddressesEvent) -> Unit,
) {
    Column {
        SwipeToDeleteContainer(
            item = address,
            onDelete = {
                onEvent(AddressesEvent.RemoveAddress(it.id))
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CheeseTheme.colors.white),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(CheeseTheme.paddings.medium)
                        .weight(1f),
                    text = address.get(),
                    style = CheeseTheme.typography.common14Regular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = {
                    onEvent(AddressesEvent.RemoveAddress(address.id))
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
                }
            }
        }
        Divider()
    }
}

@Composable
private fun AddressLoadingView() {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(CheeseTheme.paddings.medium)
                .size(24.dp),
            color = CheeseTheme.colors.accent,
            strokeWidth = 2.dp
        )
        Divider()
    }
}