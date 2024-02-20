@file:OptIn(ExperimentalLayoutApi::class)

package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog_detail_category

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.uiModels.CategoryUIModel
import ru.antares.cheese_android.presentation.components.screens.ErrorScreen
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog.CategoryItemView
import ru.antares.cheese_android.ui.theme.CheeseTheme

internal class CatalogParentCategoryPreviewProvider :
    PreviewParameterProvider<CatalogParentCategoryViewState> {
    override val values: Sequence<CatalogParentCategoryViewState> = sequenceOf(
        CatalogParentCategoryViewState.Loading(),
        CatalogParentCategoryViewState.Error(error = CatalogParentCategoryUIError.Loading("Не удалось загрузить категории")),
        CatalogParentCategoryViewState.Success(
            childCategories = listOf(
                CategoryUIModel(name = "Твердые"),
                CategoryUIModel(name = "Мягкие"),
                CategoryUIModel(name = "С плесенью"),
                CategoryUIModel(name = "Твердые"),
                CategoryUIModel(name = "Мягкие"),
                CategoryUIModel(name = "С плесенью"),
                CategoryUIModel(name = "Твердые"),
                CategoryUIModel(name = "Мягкие"),
                CategoryUIModel(name = "С плесенью"),
                CategoryUIModel(name = "Твердые"),
                CategoryUIModel(name = "Мягкие"),
                CategoryUIModel(name = "С плесенью"),
            )
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun CatalogParentCategoryScreenPreview(
    @PreviewParameter(CatalogParentCategoryPreviewProvider::class) state: CatalogParentCategoryViewState
) {
    CheeseTheme {
        CatalogParentCategoryScreen(
            navController = rememberNavController(),
            name = "Сыр",
            catalogParentCategoryViewState = state,
            onError = {

            },
            onEvent = {

            },
            navigationEvents = emptyFlow()
        )
    }
}

@Composable
fun CatalogParentCategoryScreen(
    navController: NavController,
    name: String,
    catalogParentCategoryViewState: CatalogParentCategoryViewState,
    onError: (UIError) -> Unit,
    onEvent: (CatalogParentCategoryEvent) -> Unit,
    navigationEvents: Flow<CatalogParentCategoryNavigationEvent>
) {
    ObserveAsNavigationEvents(flow = navigationEvents) { event ->
        when (event){
            is CatalogParentCategoryNavigationEvent.NavigateToProducts -> {
                navController.navigate("${Screen.CatalogNavigationGraph.Products.route}/${event.id}/${event.categoryName}")
            }
        }
    }

    CheeseTopBarWrapper(topBarContent = {
        IconButton(modifier = Modifier
            .padding(start = CheeseTheme.paddings.smallest)
            .size(CheeseTheme.paddings.large)
            .align(Alignment.CenterStart), onClick = {
            navController.popBackStack()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null
            )
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = name,
            style = CheeseTheme.typography.common16Medium
        )
    }) {
        AnimatedContent(
            targetState = catalogParentCategoryViewState,
            label = "Child categories of parent category animated content",
            transitionSpec = {
                fadeIn(tween(300)).togetherWith(fadeOut(tween(300)))
            },
            contentKey = { it.key }
        ) { state ->
            when (state) {
                is CatalogParentCategoryViewState.Error -> ErrorScreen(
                    error = state.error, retry = onError
                )

                is CatalogParentCategoryViewState.Loading -> LoadingScreen()
                is CatalogParentCategoryViewState.Success -> {
                    ParentCategoryScreenContent(
                        childCategories = state.childCategories,
                        onCategoryClick = {
                            onEvent(CatalogParentCategoryEvent.OnCategoryClick(it.id, it.name))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ParentCategoryScreenContent(
    childCategories: List<CategoryUIModel>,
    onCategoryClick: (CategoryUIModel) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp, 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        childCategories.forEach { category ->
            CategoryItemView(
                category = category,
                modifier = Modifier.weight(1f),
                onCategoryClick = onCategoryClick
            )
        }
    }
}