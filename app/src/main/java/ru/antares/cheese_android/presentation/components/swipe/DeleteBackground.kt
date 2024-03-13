@file:OptIn(ExperimentalMaterialApi::class)

package ru.antares.cheese_android.presentation.components.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 13.03.2024 at 15:23
 * Android Studio
 */

@Composable
fun DeleteBackground(
    shape: RoundedCornerShape,
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, shape)
            .padding(CheeseTheme.paddings.medium),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete swipe button icon",
            tint = CheeseTheme.colors.white
        )
    }
}