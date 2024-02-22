@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.presentation.models.CategoryUIModel
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.presentation.components.screens.ErrorScreen
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTitleWrapper
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.ui.theme.CheeseTheme

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
fun CatalogScreenPreview() {
    CheeseTheme {
        CatalogScreen(state = CatalogState(), onError = {

        }, onEvent = {

        }, navigationEvents = emptyFlow(), navController = rememberNavController())
    }
}

@Composable
fun CatalogScreen(
    navController: NavController,
    state: CatalogState,
    onError: (UIError) -> Unit,
    onEvent: (CatalogEvent) -> Unit,
    navigationEvents: Flow<CatalogNavigationEvent>
) {
    ObserveAsNavigationEvents(flow = navigationEvents) { event ->
        when (event) {
            is CatalogNavigationEvent.NavigateToProducts -> {
                navController.navigate("${Screen.CatalogNavigationGraph.Products.route}/${event.id}/${event.name}")
            }

            is CatalogNavigationEvent.OpenParentCategory -> {
                navController.navigate("${Screen.CatalogNavigationGraph.CatalogParentCategory.route}/${event.parentID}/${event.name}")
            }
        }
    }

    val (search, onSearchChange) = remember { mutableStateOf("") }

    CheeseTitleWrapper(
        title = stringResource(R.string.catalog_title),
        searchValue = search,
        onSearchChange = onSearchChange,
        enableClearButton = true,
        onSearch = { searchValue ->
            /*TODO: logic of search*/
        }
    ) {
        AnimatedContent(
            targetState = state.uiState,
            label = "Catalog screen animated content",
            transitionSpec = { fadeIn(tween(200)).togetherWith(fadeOut(tween(200))) },
        ) { uiState ->
            when (uiState) {
                CatalogUIState.ERROR -> state.error?.let { catalogUIError ->
                    ErrorScreen(
                        error = catalogUIError,
                        onError = onError
                    )
                }

                CatalogUIState.LOADING -> LoadingScreen(modifier = Modifier)
                CatalogUIState.SUCCESS -> CatalogScreenContent(
                    state = state, onEvent = onEvent, search = search
                )
            }
        }
    }
}

@Composable
private fun CatalogScreenContent(
    state: CatalogState, onEvent: (CatalogEvent) -> Unit, search: String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(state.listOfCategoryPairs) { (parent, child) ->
            PairCategoryView(
                parent = parent,
                child = child,
                onCategoryClick = {
                    onEvent(CatalogEvent.NavigateToProducts(id = it.id, name = it.name))
                },
                openParentCategory = {
                    onEvent(CatalogEvent.OpenParentCategory(parentID = parent.id, name = it.name))
                }
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
    onCategoryClick: (CategoryUIModel) -> Unit,
    openParentCategory: (CategoryUIModel) -> Unit
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
                    onCategoryClick = onCategoryClick
                )
                else if (indexCategory == 5 && child.size == 6) CategoryItemView(
                    category = category,
                    modifier = Modifier.weight(1f),
                    onCategoryClick = onCategoryClick
                ) else if (indexCategory == 5 && child.size + 5 > 6) MoreCategoryItemView(
                    category = category,
                    modifier = Modifier.weight(1f),
                    onClick = openParentCategory
                )
            }
        }
    }
}

@Composable
fun CategoryItemView(
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