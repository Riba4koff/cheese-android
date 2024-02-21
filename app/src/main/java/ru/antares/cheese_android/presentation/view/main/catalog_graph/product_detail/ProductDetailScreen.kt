package ru.antares.cheese_android.presentation.view.main.catalog_graph.product_detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * ProductDetailScreen.kt
 * @author Павел
 * Created by on 21.02.2024 at 22:01
 * Android studio
 */

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    CheeseTheme {
        ProductDetailScreen()
    }
}

@Composable
fun ProductDetailScreen() {
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

                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(id = R.string.back),
                    style = CheeseTheme.typography.common16Regular
                )
            }
        }
    ) {
        LoadingScreen()
    }
}