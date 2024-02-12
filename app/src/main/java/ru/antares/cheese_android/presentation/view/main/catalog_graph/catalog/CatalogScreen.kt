@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.presentation.components.screens.ErrorScreen
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTitleWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme
import java.util.UUID

internal class CatalogScreenPreviewProvider : PreviewParameterProvider<CatalogViewState> {
    override val values: Sequence<CatalogViewState> = sequenceOf(
        CatalogViewState.Loading(),
        CatalogViewState.Success(
            categories = (0..4).map { id ->
                CategoryUIModel(
                    id = UUID.randomUUID().toString(),
                    name = "Название категории $id",
                    position = id
                )
            },
            isLoadingNextPage = true
        ),
        CatalogViewState.Error(error = CatalogUIError.Loading("Не удалось загрузить каталог")),
    )
}

@Preview
@Composable
fun CategoryItemPreview() {
    CheeseTheme {
        CategoryItemView(
            category = CategoryUIModel(name = "Сырная тарелка с кусочками винограда"),
            onCategoryClick = {

            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview(
    @PreviewParameter(CatalogScreenPreviewProvider::class) state: CatalogViewState
) {
    CheeseTheme {
        CatalogScreen(
            state = state,
            onError = {

            },
            onEvent = {

            }
        )
    }
}

@Composable
fun CatalogScreen(
    state: CatalogViewState,
    onError: (UIError) -> Unit,
    onEvent: (CatalogEvent) -> Unit
) {
    val (search, onSearchChange) = remember { mutableStateOf("") }

    CheeseTitleWrapper(
        title = stringResource(R.string.catalog_title),
        searchValue = search,
        onSearchChange = onSearchChange,
        enableClearButton = true,
        onSearch = { searchValue ->

        }
    ) {
        AnimatedContent(
            targetState = state,
            label = "Catalog screen animated content",
            transitionSpec = { fadeIn(tween(200)).togetherWith(fadeOut(tween(200))) },
            contentKey = { it.key }
        ) { uiState ->
            when (uiState) {
                is CatalogViewState.Error -> ErrorScreen(error = uiState.error, retry = onError)
                is CatalogViewState.Loading -> LoadingScreen(modifier = Modifier)
                is CatalogViewState.Success -> CatalogScreenContent(
                    state = uiState,
                    onEvent = onEvent,
                    search = search
                )
            }
        }
    }
}

@Composable
private fun CatalogScreenContent(
    state: CatalogViewState.Success,
    onEvent: (CatalogEvent) -> Unit,
    search: String
) {
    val lazyGridState = rememberLazyGridState()

    val categoryUIModelListHashMap: HashMap<CategoryUIModel, List<CategoryUIModel>> = hashMapOf(
        Pair(
            CategoryUIModel(name = "Сыры"),
            listOf(
                CategoryUIModel(name = "Мягкие"),
                CategoryUIModel(name = "Твердые"),
                CategoryUIModel(name = "С плесенью")
            )
        )
    )

    Column {
        /*Column(Modifier.verticalScroll(rememberScrollState())) {
            for ((parent, childCategories) in categoryUIModelListHashMap) {
                Row(Modifier.fillMaxWidth().padding(14.dp, 0.dp)) {
                    if (childCategories.isNotEmpty()) {
                        Text(
                            text = parent.name, style = CheeseTheme.typography.common24Medium
                        )
                    }
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth().padding(14.dp, 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    childCategories.forEachIndexed { indexCategory, category ->
                        if (indexCategory < 5) CategoryItemView(
                            category = category,
                            modifier = Modifier.weight(1f),
                            onCategoryClick = {}
                        ) else if (indexCategory == 5 && childCategories.size == 6) CategoryItemView(
                            category = category,
                            modifier = Modifier.weight(1f),
                            onCategoryClick = {}
                        ) else if (indexCategory == 5 && childCategories.size + 5 > 6) CategoryItemView(
                            category = category,
                            modifier = Modifier.weight(1f),
                            onCategoryClick = {}
                        )
                    }
                }
            }
        }*/
        LazyVerticalGrid(
            state = lazyGridState,
            horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest),
            columns = GridCells.Adaptive(162.dp),
            contentPadding = PaddingValues(horizontal = CheeseTheme.paddings.medium),
            content = {
                items(state.categories) { category ->
                    CategoryItemView(
                        category = category,
                        modifier = Modifier.weight(1f),
                        onCategoryClick = { cat ->
                             /*TODO: handle click to category*/
                        }
                    )
                }
                item {
                    LoadingCategoryItemView(isLoading = state.isLoadingNextPage)
                }
            }
        )
        /*FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp, 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.categories.forEach { category ->
                CategoryItemView(
                    category = category,
                    modifier = Modifier.weight(1f)
                )
            }
            LoadingCategoryItemView(state.isLoadingNextPage)
        }*/
    }
}

@Composable
private fun CategoryItemView(
    modifier: Modifier = Modifier,
    category: CategoryUIModel,
    onCategoryClick: (CategoryUIModel) -> Unit
) {
    val gradientColor = arrayOf(
        0.0f to Color.Transparent,
        0.5f to Color.Black.copy(0.2f),
        0.8f to Color.Black.copy(0.8f),
        1.0f to Color.Black.copy(1f)
    )

    Card(
        modifier = modifier
            .height(270.dp)
            .width(160.dp)
            .padding(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = {
            onCategoryClick(category)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // TODO: - change to async image
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.category_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(colorStops = gradientColor))
            )
            Text(
                text = category.name,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(0.8f),
                style = CheeseTheme.typography.common16Medium,
                color = CheeseTheme.colors.white,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LoadingCategoryItemView(isLoading: Boolean) {
    val gradientColor = arrayOf(
        0.0f to Color.Transparent,
        0.5f to Color.Black.copy(0.2f),
        0.8f to Color.Black.copy(0.8f),
        1.0f to Color.Black.copy(1f)
    )

    if (isLoading) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .height(250.dp)
                    .width(162.dp)
                    .padding(2.dp), contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(isLoading = true)
            }
        }
    }
}