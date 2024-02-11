package ru.antares.cheese_android.presentation.components.buttons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun CheeseButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    textStyle: TextStyle = CheeseTheme.typography.common16Medium,
    shape: RoundedCornerShape = CheeseTheme.shapes.medium,
    onClick: () -> Unit
) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = CheeseTheme.colors.accent,
        contentColor = CheeseTheme.colors.black,
        disabledContainerColor = CheeseTheme.colors.accent.copy(0.5f),
        disabledContentColor = CheeseTheme.colors.gray
    )

    Button(
        modifier = modifier,
        onClick = onClick,
        colors = buttonColors,
        shape = shape,
        enabled = enabled
    ) {
        Text(text = text, style = textStyle)
    }
}

@Preview
@Composable
fun CheeseButtonPreview() {
    CheeseTheme {
        CheeseButton(text = "Кнопка") {

        }
    }
}