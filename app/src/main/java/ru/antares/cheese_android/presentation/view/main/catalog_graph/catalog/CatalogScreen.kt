@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import okhttp3.internal.filterList
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.models.uiModels.catalog.CategoryUIModel
import ru.antares.cheese_android.isFirstItemVisible
import ru.antares.cheese_android.isLastItemVisible
import ru.antares.cheese_android.isScrolledToTheEnd
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.presentation.components.screens.ErrorScreen
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTitleWrapper
import ru.antares.cheese_android.rememberScrollContext
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
            }, isLoadingNextPage = true, listOfCategoryPairs = listOf(
                Pair(
                    CategoryUIModel(name = "Название категории"), listOf(
                        CategoryUIModel(name = "Название категории"),
                        CategoryUIModel(name = "Название категории"),
                        CategoryUIModel(name = "Название категории"),
                        CategoryUIModel(name = "Название категории"),
                        CategoryUIModel(name = "Название категории"),
                    )
                )
            )
        ),
        CatalogViewState.Error(error = CatalogUIError.Loading("Не удалось загрузить каталог")),
    )
}

@Preview
@Composable
fun CategoryItemPreview() {
    CheeseTheme {
        CategoryItemView(category = CategoryUIModel(name = "Сырная тарелка с кусочками винограда"),
            onCategoryClick = {

            })
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview(
    @PreviewParameter(CatalogScreenPreviewProvider::class) state: CatalogViewState
) {
    CheeseTheme {
        CatalogScreen(state = state, onError = {

        }, onEvent = {

        })
    }
}

@Composable
fun CatalogScreen(
    state: CatalogViewState, onError: (UIError) -> Unit, onEvent: (CatalogEvent) -> Unit
) {
    val (search, onSearchChange) = remember { mutableStateOf("") }

    CheeseTitleWrapper(title = stringResource(R.string.catalog_title),
        searchValue = search,
        onSearchChange = onSearchChange,
        enableClearButton = true,
        onSearch = { searchValue ->

        }) {
        AnimatedContent(targetState = state,
            label = "Catalog screen animated content",
            transitionSpec = { fadeIn(tween(200)).togetherWith(fadeOut(tween(200))) },
            contentKey = { it.key }) { uiState ->
            when (uiState) {
                is CatalogViewState.Error -> ErrorScreen(error = uiState.error, retry = onError)
                is CatalogViewState.Loading -> LoadingScreen(modifier = Modifier)
                is CatalogViewState.Success -> CatalogScreenContent(
                    state = uiState, onEvent = onEvent, search = search
                )
            }
        }
    }
}

@Composable
private fun CatalogScreenContent(
    state: CatalogViewState.Success, onEvent: (CatalogEvent) -> Unit, search: String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(state.listOfCategoryPairs) { (parent, child) ->
            PairCategoryView(
                parent = parent,
                child = child,
            )
        }
        // TODO: - loading for pagination
        /*item {
            LoadingIndicator(
                isLoading = state.isLoadingNextPage,
                showBackground = false
            )
        }*/
    }
}

@Composable
fun PairCategoryView(
    parent: CategoryUIModel,
    child: List<CategoryUIModel>,
) {
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp, vertical = 8.dp
                )
        ) {
            if (child.isNotEmpty()) Text(
                text = parent.name, style = CheeseTheme.typography.common24Medium
            )
        }
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp, 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            child.forEachIndexed { indexCategory, category ->
                if (indexCategory < 5) CategoryItemView(
                    category = category,
                    modifier = Modifier.weight(1f),
                    onCategoryClick = {

                    }) else if (indexCategory == 5 && child.size == 6) CategoryItemView(
                    category = category,
                    modifier = Modifier.weight(1f),
                    onCategoryClick = {

                    }) else if (indexCategory == 5 && child.size + 5 > 6) MoreCategoryItemView(
                    category = category,
                    modifier = Modifier.weight(1f),
                    onClick = {

                    }
                )
            }
        }
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

    Card(modifier = modifier
        .height(270.dp)
        .width(160.dp)
        .padding(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = {
            onCategoryClick(category)
        }) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current).data(category.imageLink)
                    .crossfade(true).build(),
                contentDescription = "Category image",
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
private fun LoadingCategoryItemView(modifier: Modifier = Modifier, isLoading: Boolean) {
    if (isLoading) {
        Card(
            modifier = modifier
                .height(250.dp)
                .width(162.dp)
                .padding(2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier, contentAlignment = Alignment.Center
            ) {
                LoadingIndicator(isLoading = true)
            }
        }
    }
}

@Composable
fun MoreCategoryItemView(
    modifier: Modifier = Modifier,
    category: CategoryUIModel,
    onClick: (CategoryUIModel) -> Unit
) {
    val gradientColor = arrayOf(
        0.0f to Color.Transparent,
        0.5f to Color.Black.copy(0.2f),
        0.8f to Color.Black.copy(0.8f),
        1.0f to Color.Black.copy(1f)
    )

    Card(modifier = modifier
        .height(270.dp)
        .width(160.dp)
        .padding(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = {
            onClick(category)
        }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current).data(category.imageLink)
                        .crossfade(true).build(),
                    contentDescription = "Category image",
                    contentScale = ContentScale.Crop
                )
                // Vertical black gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(colorStops = gradientColor))
                )
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.more_categories),
                style = CheeseTheme.typography.common18Bold,
                color = CheeseTheme.colors.white
            )
        }
    }
}