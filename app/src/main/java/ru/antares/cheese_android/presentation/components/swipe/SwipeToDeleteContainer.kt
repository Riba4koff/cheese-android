@file:OptIn(ExperimentalMaterialApi::class)

package ru.antares.cheese_android.presentation.components.swipe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * @author pavelrybakov
 * Created 13.03.2024 at 15:26
 * Android Studio
 */

@Composable
fun <T> SwipeToDeleteContainer(
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable () -> Unit
) {
    var isRemoved by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState(
        confirmStateChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = fadeOut() + shrinkVertically(tween(animationDuration), Alignment.Top)
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                DeleteBackground(shape = shape, swipeDismissState = dismissState)
            },
            dismissContent = { content() },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}