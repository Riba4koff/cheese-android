package ru.antares.cheese_android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.antares.cheese_android.shimmerLoadingAnimation
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * ShimmerLoadingBox.kt
 * @author Павел
 * Created by on 06.05.2024 at 20:39
 * Android studio
 */

@Composable
fun ShimmerLoadingBox(
    modifier: Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(0)
) {
    Box(modifier = modifier
        .background(color = Color.LightGray, shape = shape)
        .shimmerLoadingAnimation())
}