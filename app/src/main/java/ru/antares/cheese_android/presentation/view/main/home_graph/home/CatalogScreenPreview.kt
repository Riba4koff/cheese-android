package ru.antares.cheese_android.presentation.view.main.home_graph.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTitleWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CheeseTheme {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    CheeseTitleWrapper(title = stringResource(R.string.home_title)) {

    }
}