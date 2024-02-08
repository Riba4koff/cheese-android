package ru.antares.cheese_android.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.onClick
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier, isLoading: Boolean) {
    val indicatorSize = 24.dp

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier
                .background(
                    CheeseTheme.colors.gray.copy(0.7f),
                    CheeseTheme.shapes.small
                )
                .padding(CheeseTheme.paddings.medium)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(indicatorSize),
                color = CheeseTheme.colors.accent,
                strokeWidth = 2.dp
            )
        }
    }
}

@Preview()
@Composable
fun LoadingIndicatorPreview() {
    CheeseTheme {
        LoadingIndicator(isLoading = true)
    }
}