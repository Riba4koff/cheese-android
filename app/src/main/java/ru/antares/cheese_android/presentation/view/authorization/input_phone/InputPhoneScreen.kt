package ru.antares.cheese_android.presentation.view.authorization.input_phone

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.antares.cheese_android.ObserveAsEvents
import ru.antares.cheese_android.PhoneVisualTransformation
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.ErrorAlertDialog
import ru.antares.cheese_android.presentation.LoadingIndicator
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.authorization.AgreementText
import ru.antares.cheese_android.presentation.view.authorization.confirm_code.SkipAuthorizationButton
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview
@Composable
fun InputPhoneScreenPreview() {
    CheeseTheme {
        InputPhoneScreen(
            state = InputPhoneState(
                isLoading = true,
                error = ErrorState(
                    isError = false,
                    message = "Сервер не отвечает"
                )
            ),
            onEvent = {

            },
            navController = rememberNavController(),
            navigationEvents = emptyFlow()
        )
    }
}

@Composable
fun InputPhoneScreen(
    navController: NavController,
    state: InputPhoneState,
    onEvent: (Event) -> Unit,
    navigationEvents: Flow<NavigationEvent>,
) {
    ObserveAsEvents(flow = navigationEvents) { event ->
        when (event) {
            is NavigationEvent.NavigateToConfirmCode -> {
                val validPhone = "+7${event.phone}"
                navController.navigate("${Screen.AuthNavigationGraph.ConfirmCode.route}/$validPhone")
            }

            NavigationEvent.NavigateToHomeScreen -> {
                navController.navigate(Screen.HomeNavigationGraph.route) {
                    popUpTo(Screen.AuthNavigationGraph.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AuthorizationBackground(image = R.drawable.auth_background)

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.padding(
                    top = CheeseTheme.paddings.large + CheeseTheme.paddings.large
                ),
                painter = painterResource(id = R.drawable.authorization_logo),
                contentDescription = "Authorization logo"
            )

            InputPhoneScreenContent(
                phone = state.phone,
                onPhoneChange = { phone ->
                    onEvent(Event.OnPhoneChange(phone))
                },
                phoneIsNotValid = state.phoneIsValid.not()
            )

            Spacer(modifier = Modifier.height(64.dp))

            AgreementText(
                onPrivacyPolicyClick = {

                },
                onAgreementClick = {

                }
            )
        }

        SkipAuthorizationButton(modifier = Modifier.align(Alignment.TopEnd)) {
            onEvent(Event.SkipAuthorization)
        }

        if (state.error.isError) ErrorAlertDialog(
            errorMessage = state.error.message,
            onDismissRequest = {
                onEvent(Event.CloseAlertDialog)
            }
        )

        LoadingIndicator(isLoading = state.isLoading)
    }
}

@Composable
private fun InputPhoneScreenContent(
    phone: String,
    onPhoneChange: (String) -> Unit,
    phoneIsNotValid: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CheeseTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.phone),
            style = CheeseTheme.textStyles.common12Light,
            color = CheeseTheme.colors.white
        )
        PhoneField(
            mask = "+7 (000) 000-00-00",
            maskNumber = '0',
            onPhoneChange = { onPhoneChange(it) },
            phone = phone,
            phoneIsNotValid = phoneIsNotValid
        )
    }
}

@Composable
private fun PhoneField(
    modifier: Modifier = Modifier,
    phone: String,
    mask: String = "+7 (000) 000-00-00",
    maskNumber: Char = '0',
    onPhoneChange: (String) -> Unit,
    phoneIsNotValid: Boolean
) {
    val focus = LocalFocusManager.current

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = CheeseTheme.colors.white,
        unfocusedContainerColor = CheeseTheme.colors.white,
        errorContainerColor = CheeseTheme.colors.white,
        focusedIndicatorColor = CheeseTheme.colors.accent,
        unfocusedLabelColor = CheeseTheme.colors.accent
    )

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = phone,
        onValueChange = { value ->
            if (value.length == 10) focus.clearFocus()
            if (value.length < 11) {
                onPhoneChange(value)
            }
        },
        colors = textFieldColors,
        shape = CheeseTheme.shapes.small,
        isError = phoneIsNotValid,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        textStyle = CheeseTheme.textStyles.common16Medium,
        placeholder = {
            Text(
                text = "+7",
                style = CheeseTheme.textStyles.common16Light,
                color = Color.Gray
            )
        }
    )
}

@Composable
fun AuthorizationBackground(image: Int) {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = image),
        contentDescription = "Authorization image background",
        contentScale = ContentScale.Crop
    )
}
