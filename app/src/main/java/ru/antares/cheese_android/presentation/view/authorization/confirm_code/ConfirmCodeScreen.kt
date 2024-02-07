package ru.antares.cheese_android.presentation.view.authorization.confirm_code

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
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
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.ErrorAlertDialog
import ru.antares.cheese_android.presentation.LoadingIndicator
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.presentation.view.authorization.AgreementText
import ru.antares.cheese_android.presentation.view.authorization.input_phone.AuthorizationBackground
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview
@Composable
fun ConfirmCodePreview() {
    CheeseTheme {
        ConfirmCodeScreen(
            navController = rememberNavController(),
            state = ConfirmCodeState(
                isLoading = false,
                codeIsWrong = false,
                timer = 0,
                canMakeCallAgain = true
            ),
            onEvent = {

            },
            navigationEvents = emptyFlow(),

            )
    }
}

@Composable
fun ConfirmCodeScreen(
    navController: NavController,
    state: ConfirmCodeState,
    onEvent: (ConfirmCodeEvent) -> Unit,
    navigationEvents: Flow<ConfirmCodeNavigationEvent>,
) {
    ObserveAsEvents(flow = navigationEvents) { event ->
        when (event) {
            ConfirmCodeNavigationEvent.NavigateToHomeScreen -> {
                navController.navigate(Screen.HomeNavigationGraph.route) {
                    popUpTo(Screen.AuthNavigationGraph.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
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

            ConfirmCodeScreenContent(
                code = state.code,
                onCodeChange = { code ->
                    onEvent(ConfirmCodeEvent.OnCodeChange(code))
                }, codeIsWrong = state.codeIsWrong,
                canMakeCallAgain = state.canMakeCallAgain,
                makeCallAgain = {
                    onEvent(ConfirmCodeEvent.MakeCallAgain)
                }, timer = state.timer
            )

            Spacer(modifier = Modifier.height(64.dp))

            AgreementText(
                onPrivacyPolicyClick = {

                },
                onAgreementClick = {

                }
            )
        }

        PopBackButton(modifier = Modifier.align(Alignment.TopStart)) {
            navController.popBackStack()
        }

        SkipAuthorizationButton(modifier = Modifier.align(Alignment.TopEnd)) {
            onEvent(ConfirmCodeEvent.SkipAuthorization)
        }

        if (state.error.isError) ErrorAlertDialog(
            errorMessage = state.error.message,
            onDismissRequest = {
                onEvent(ConfirmCodeEvent.CloseAlertDialog)
            }
        )

        LoadingIndicator(isLoading = state.isLoading)
    }
}

@Composable
private fun PopBackButton(modifier: Modifier, onClick: () -> Unit) {
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        label = "Animation of pressing"
    )

    Text(
        modifier = modifier
            .padding(CheeseTheme.paddings.medium)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    onPressedChange(true)
                    tryAwaitRelease()
                    onPressedChange(false)
                }, onTap = {
                    onClick()
                })
            }
            .scale(scale),
        text = stringResource(R.string.back),
        style = CheeseTheme.textStyles.common14Medium,
        color = CheeseTheme.colors.white
    )

}

@Composable
fun SkipAuthorizationButton(modifier: Modifier, onClick: () -> Unit) {
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        label = "Animation of pressing"
    )

    Text(
        modifier = modifier
            .padding(CheeseTheme.paddings.medium)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    onPressedChange(true)
                    tryAwaitRelease()
                    onPressedChange(false)
                }, onTap = {
                    onClick()
                })
            }
            .scale(scale),
        text = stringResource(R.string.skip),
        style = CheeseTheme.textStyles.common14Medium,
        color = CheeseTheme.colors.white
    )
}

@Composable
private fun ConfirmCodeScreenContent(
    code: String,
    onCodeChange: (String) -> Unit,
    codeIsWrong: Boolean,
    canMakeCallAgain: Boolean,
    timer: Int,
    makeCallAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = CheeseTheme.paddings.medium),
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
    ) {
        Text(
            text = stringResource(R.string.input_last_fourth_numbers_of_phone),
            style = CheeseTheme.textStyles.common12Light,
            color = CheeseTheme.colors.white
        )
        CodeField(
            onCodeChange = { onCodeChange(it) },
            phone = code,
            codeIsWrong = codeIsWrong
        )
        TimerBody {
            if (canMakeCallAgain) MakeCallAgainText {
                makeCallAgain()
            } else Timer(timer = timer)
        }
    }
}

@Composable
private fun TimerBody(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        content()
    }
}

@Composable
private fun MakeCallAgainText(onClick: () -> Unit) {
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        label = "Animation of pressing"
    )
    Text(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    onPressedChange(true)
                    tryAwaitRelease()
                    onPressedChange(false)
                }, onTap = {
                    onClick()
                })
            }
            .scale(scale),
        text = stringResource(R.string.make_call_again),
        style = CheeseTheme.textStyles.common12Light.copy(color = CheeseTheme.colors.white)
    )
}

@Composable
private fun Timer(
    timer: Int
) {
    Text(
        text = "Отправить код повторно через ${timer} секунд",
        style = CheeseTheme.textStyles.common12Light,
        color = CheeseTheme.colors.white
    )
}

@Composable
private fun CodeField(
    modifier: Modifier = Modifier,
    phone: String,
    onCodeChange: (String) -> Unit,
    codeIsWrong: Boolean
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
            if (value.length == 4) focus.clearFocus()
            if (value.length < 5) {
                onCodeChange(value)
            }
        },
        colors = textFieldColors,
        shape = CheeseTheme.shapes.small,
        isError = codeIsWrong,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        textStyle = CheeseTheme.textStyles.common16Medium
    )

    AnimatedVisibility(visible = codeIsWrong, enter = fadeIn(), exit = fadeOut()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = stringResource(R.string.wrong_confirm_code),
                style = CheeseTheme.textStyles.common12Light.copy(color = CheeseTheme.colors.red)
            )
        }
    }
}