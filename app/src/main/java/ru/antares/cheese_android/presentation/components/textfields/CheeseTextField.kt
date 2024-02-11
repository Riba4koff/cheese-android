package ru.antares.cheese_android.presentation.components.textfields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.validators.ValidationTextFieldResult
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun CheeseTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    validationTextFieldResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    backgroundColor: Color = Color.Transparent,
    errorAlignment: Alignment = Alignment.CenterStart,
    leadingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shape: RoundedCornerShape = CheeseTheme.shapes.small,
    textStyle: TextStyle = CheeseTheme.typography.common16Light
) {
    val textFieldColors = TextFieldDefaults.colors(
        cursorColor = CheeseTheme.colors.accent,
        errorContainerColor = CheeseTheme.colors.red.copy(0.05f),
        unfocusedContainerColor = backgroundColor,
        focusedContainerColor = backgroundColor,
        focusedIndicatorColor = CheeseTheme.colors.accent,
        selectionColors = TextSelectionColors(
            handleColor = CheeseTheme.colors.accent,
            backgroundColor = CheeseTheme.colors.gray.copy(0.2f)
        )
    )

    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            isError = validationTextFieldResult.success.not(),
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            placeholder = {
                Text(text = placeholder)
            },
            colors = textFieldColors,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            shape = shape,
            textStyle = textStyle
        )
        AnimatedVisibility(
            visible = validationTextFieldResult.success.not(),
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                validationTextFieldResult.text?.let {
                    Text(
                        modifier = Modifier
                            .padding(top = CheeseTheme.paddings.smallest)
                            .align(errorAlignment),
                        text = stringResource(id = validationTextFieldResult.text),
                        style = CheeseTheme.typography.common12Light,
                        color = CheeseTheme.colors.red
                    )
                }
            }
        }
    }
}

/**
* Unfocused cheese text field
* */
@Preview(showBackground = true)
@Composable
fun CheeseUnfocusedTextFieldPreview() {
    CheeseTheme {
        CheeseTextField(
            modifier = Modifier.padding(4.dp),
            value = "1234",
            onValueChange = {},
            placeholder = ""
        )
    }
}

/**
 * Unfocused cheese text field with placeholder
 * */
@Preview(showBackground = true)
@Composable
fun CheeseUnfocusedTextFieldWithPlaceholderPreview() {
    CheeseTheme {
        CheeseTextField(
            modifier = Modifier.padding(4.dp),
            value = "",
            onValueChange = {},
            placeholder = ""
        )
    }
}

/**
 * Cheese text field with error state but without text error
 * */
@Preview(showBackground = true)
@Composable
fun CheeseErrorTextFieldWithoutTextPreview() {
    CheeseTheme {
        CheeseTextField(
            modifier = Modifier.padding(4.dp),
            value = "",
            onValueChange = {},
            placeholder = ""
        )
    }
}

/**
 * Cheese text field with error state and without text error
 * alignment text error - start
 * */
@Preview(showBackground = true)
@Composable
fun CheeseErrorTextFieldWithTextErrorLeftALignPreview() {
    CheeseTheme {
        CheeseTextField(
            modifier = Modifier.padding(4.dp),
            value = "",
            onValueChange = {},
            placeholder = "",
            validationTextFieldResult = ValidationTextFieldResult(
                text = R.string.the_field_must_not_be_empty,
                success = false
            ),
            errorAlignment = Alignment.CenterStart
        )
    }
}

/**
 * Cheese text field with error state and without text error
 * alignment text error - center
 * */
@Preview(showBackground = true)
@Composable
fun CheeseErrorTextFieldWithErrorTextRightAlignPreview() {
    CheeseTheme {
        CheeseTextField(
            modifier = Modifier.padding(4.dp),
            value = "",
            onValueChange = {},
            placeholder = "",
            validationTextFieldResult = ValidationTextFieldResult(
                text = R.string.the_field_must_not_be_empty,
                success = false
            ),
            errorAlignment = Alignment.Center
        )
    }
}

/**
 * Cheese text field with error state and without text error
 * alignment text error - end
 * */
@Preview(showBackground = true)
@Composable
fun CheeseErrorTextFieldWithTextErrorCenterAlignPreview() {
    CheeseTheme {
        CheeseTextField(
            modifier = Modifier.padding(4.dp),
            value = "",
            onValueChange = {},
            placeholder = "",
            validationTextFieldResult = ValidationTextFieldResult(
                text = R.string.the_field_must_not_be_empty,
                success = false
            ),
            errorAlignment = Alignment.CenterEnd
        )
    }
}








