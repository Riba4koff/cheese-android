@file:OptIn(ExperimentalFoundationApi::class)

package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.view.main.catalog_graph.products.ProductsUIState.*
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author: Павел
 * Date: 20.02.2024.
 */

@Composable
fun ProductsScreen(
    name: String,
    navController: NavController,
    state: ProductsState,
    onEvent: (ProductsEvent) -> Unit,
    onError: (UIError) -> Unit
) {
    CheeseTopBarWrapper(topBarContent = {
        IconButton(modifier = Modifier
            .padding(start = CheeseTheme.paddings.smallest)
            .size(CheeseTheme.paddings.large)
            .align(Alignment.CenterStart), onClick = {
            navController.popBackStack()
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
    }) {
        AnimatedContent(
            targetState = state.loading,
            label = "Products state animated content",
            transitionSpec = {
                fadeIn(tween(200)).togetherWith(fadeOut(tween(200)))
            }
        ) { uiState ->
            when (uiState) {
                true -> {
                    LoadingScreen()
                }

                false -> {
                    if (state.products.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
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
                            itemsIndexed(
                                items = state.products,
                                key = { _, it -> it.id }
                            ) { index, product ->
                                Box(
                                    modifier = Modifier
                                        .animateItemPlacement(animationSpec = tween(200))
                                        .fillMaxWidth()
                                        .aspectRatio(2f / 1f)
                                        .background(Color.Red.copy(0.1f), CheeseTheme.shapes.small),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = product.name)
                                }
                                if (index >= state.products.size - 1 && !state.loadingNextPage && !state.endReached) onEvent(
                                    ProductsEvent.LoadNextPage(page = state.currentPage + 1, size = state.pageSize)
                                )
                            }
                            item {
                                if (state.loadingNextPage && !state.endReached) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(2f / 1f)
                                            .background(
                                                Color.LightGray.copy(0.1f),
                                                CheeseTheme.shapes.small
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = CheeseTheme.colors.accent,
                                            strokeWidth = 2.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}