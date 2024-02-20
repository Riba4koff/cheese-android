package ru.antares.cheese_android.presentation.components.textfields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import ru.antares.cheese_android.PhoneVisualTransformation
import ru.antares.cheese_android.domain.validators.ValidationTextFieldResult
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun PhoneTextField(
    modifier: Modifier = Modifier,
    phone: String,
    mask: String = "+7 (000) 000-00-00",
    maskNumber: Char = '0',
    onPhoneChange: (String) -> Unit,
    validationTextFieldResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    enabled: Boolean = true
) {
    val focus = LocalFocusManager.current

    CheeseTextField(
        modifier = modifier,
        value = phone,
        onValueChange = { value ->
            if (value.length == 10) focus.clearFocus()
            if (value.length < 11) {
                onPhoneChange(value)
            }
        },
        placeholder = "+7",
        shape = CheeseTheme.shapes.small,
        validationTextFieldResult = validationTextFieldResult,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        textStyle = CheeseTheme.typography.common16Medium,
        backgroundColor = CheeseTheme.colors.white,
        enabled = enabled
    )
}