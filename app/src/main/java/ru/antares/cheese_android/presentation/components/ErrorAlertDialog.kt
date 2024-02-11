package ru.antares.cheese_android.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.R
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun ErrorAlertDialog(
    errorMessage: String,
    confirmText: String = "OK",
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
                    Text(confirmText)
                }
            }
            Spacer(modifier = Modifier.height(CheeseTheme.paddings.small))
        }
    })
}

@Preview
@Composable
fun ErrorAlertDialogPreview() {
    CheeseTheme {
        ErrorAlertDialog(errorMessage = "Какая - то") {
            
        }
    }
}








