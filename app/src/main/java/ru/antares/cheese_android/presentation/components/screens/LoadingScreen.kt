package ru.antares.cheese_android.presentation.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.presentation.components.LoadingIndicator
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, hasBackground: Boolean = false) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (hasBackground) CheeseTheme.colors.gray.copy(0.3f)
                else Color.Transparent
            ),
        contentAlignment = Alignment.Center
    ) {
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