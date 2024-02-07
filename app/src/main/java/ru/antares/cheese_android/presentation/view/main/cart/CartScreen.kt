package ru.antares.cheese_android.presentation.view.main.cart

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.CheeseTitle
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    CheeseTheme {
        CartScreen()
    }
}

@Composable
fun CartScreen() {
    CheeseTitle(title = stringResource(R.string.cart_title)) {

    }
}