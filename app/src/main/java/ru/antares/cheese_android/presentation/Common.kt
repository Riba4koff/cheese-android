package ru.antares.cheese_android.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.onClick
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun ErrorAlertDialog(
    errorMessage: String,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(onDismissRequest = {
        onDismissRequest()
    }, title = {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.error_occured),
                    style = CheeseTheme.textStyles.common18Bold
                )
            }
            Spacer(modifier = Modifier.height(CheeseTheme.paddings.medium))
        }
    }, text = {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    style = CheeseTheme.textStyles.common14Light,
                    textAlign = TextAlign.Center
                )
            }
        }
    }, confirmButton = {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val buttonColors = ButtonDefaults.buttonColors(
                    containerColor = CheeseTheme.colors.accent,
                    contentColor = CheeseTheme.colors.black
                )
                Button(
                    onClick = { onDismissRequest() },
                    shape = CheeseTheme.shapes.small,
                    colors = buttonColors
                ) {
                    Text(stringResource(R.string.continue_title))
                }
            }
            Spacer(modifier = Modifier.height(CheeseTheme.paddings.small))
        }
    })
}

@Composable
fun LoadingIndicator(isLoading: Boolean) {
    val indicatorSize = 24.dp

    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier
            .onClick { /*DO NOTHING*/ }
            .fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(
                        CheeseTheme.colors.gray.copy(0.7f),
                        CheeseTheme.shapes.small
                    )
                    .padding(CheeseTheme.paddings.medium)
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(indicatorSize),
                    color = CheeseTheme.colors.accent,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}


@Composable
fun LoadingScreen(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LoadingIndicator(isLoading = true)
    }
}

@Composable
fun CheeseTitle(title: String, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(CheeseTheme.paddings.medium + CheeseTheme.paddings.small),
            text = title,
            style = CheeseTheme.textStyles.largeTitle
        )
        content()
    }
}