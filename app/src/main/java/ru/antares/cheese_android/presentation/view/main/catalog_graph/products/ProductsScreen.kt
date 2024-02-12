package ru.antares.cheese_android.presentation.view.main.catalog_graph.products

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
import androidx.navigation.NavController
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun ProductsScreen(
    name: String,
    navController: NavController
) {
    CheeseTopBarWrapper(
        topBarContent = {
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
        }
    ) {

    }
}