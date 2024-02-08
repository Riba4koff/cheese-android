package ru.antares.cheese_android.presentation.components.textfields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun CheeseSearchTextFieldPreview() {
    CheeseTheme {
        CheeseSearchTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = "",
            onValueChange = {},
            enableClearButton = true,
            search = {

            }
        )
    }
}

/**
 * Cheese search text field
 *
 * Created Pavel Rybakov 20:19 08.02.2024
 * */
@Composable
fun CheeseSearchTextField(
    modifier: Modifier = Modifier,
    search: (String) -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Давайте найдем что-нибудь",
    keyboardActions: KeyboardActions = KeyboardActions(
        onSearch = {
            search(value)
        }
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search
    ),
    enableClearButton: Boolean = false,
    shape: RoundedCornerShape = CheeseTheme.shapes.small,
) {
    val colors = TextFieldDefaults.colors(
        cursorColor = CheeseTheme.colors.gray,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedContainerColor = CheeseTheme.colors.gray.copy(0.12f),
        focusedContainerColor = CheeseTheme.colors.gray.copy(0.12f),
        selectionColors = TextSelectionColors(
            handleColor = CheeseTheme.colors.accent,
            backgroundColor = CheeseTheme.colors.gray.copy(0.2f)
        )
    )

    OutlinedTextField(
        modifier = modifier.height(56.dp),
        value = value,
        onValueChange = onValueChange,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        placeholder = {
            Text(
                text = placeholder,
                style = CheeseTheme.textStyles.common14Light,
                color = CheeseTheme.colors.gray
            )
        },
        trailingIcon = {
            Box {
                AnimatedVisibility(
                    visible = enableClearButton && value.isNotBlank(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        onClick = {
                            onValueChange("")
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            tint = CheeseTheme.colors.gray
                        )
                    }
                }
                AnimatedVisibility(
                    visible = value.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = CheeseTheme.colors.gray
                    )
                }
            }
        },
        shape = shape,
        colors = colors,
        textStyle = CheeseTheme.textStyles.common14Light
    )
}