package ru.antares.cheese_android.presentation.view.main.catalog_graph.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTitleWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun CatalogScreenPreview() {
    CheeseTheme {
        CatalogScreen()
    }
}

@Composable
fun CatalogScreen() {
    CheeseTitleWrapper(title = stringResource(R.string.catalog_title)) {

    }
}