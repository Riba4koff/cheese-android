package ru.antares.cheese_android.presentation.components.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LoadingIndicator(isLoading = true)
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    CheeseTheme {
        LoadingScreen(modifier = Modifier)
    }
}