@file:OptIn(ExperimentalAnimationApi::class)

package ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ru.antares.cheese_android.ObserveAsNavigationEvents
import ru.antares.cheese_android.PhoneVisualTransformation
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.domain.validators.ValidationTextFieldResult
import ru.antares.cheese_android.presentation.components.buttons.CheeseButton
import ru.antares.cheese_android.presentation.components.screens.ErrorScreen
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.shaker.ShakeConfig
import ru.antares.cheese_android.presentation.components.shaker.ShakeController
import ru.antares.cheese_android.presentation.components.shaker.rememberShakeController
import ru.antares.cheese_android.presentation.components.shaker.shake
import ru.antares.cheese_android.presentation.components.textfields.CheeseTextField
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme

internal class PersonalDataScreenPreviewProvider : PreviewParameterProvider<PersonalDataViewState> {
    override val values: Sequence<PersonalDataViewState> = sequenceOf(
        PersonalDataViewState.Loading(),
        PersonalDataViewState.Error(PersonalDataUIError.SomeError("Неизвестная ошибка")),
        PersonalDataViewState.Success()
    )
}

@Preview(showBackground = true)
@Composable
fun PersonalDataPreview(
    @PreviewParameter(PersonalDataScreenPreviewProvider::class) state: PersonalDataViewState
) {
    CheeseTheme {
        PersonalDataScreen(
            state = state,
            onEvent = {

            },
            onError = {

            },
            navController = rememberNavController(),
            navigationEvents = emptyFlow()
        )
    }
}

@Composable
fun PersonalDataScreen(
    state: PersonalDataViewState,
    onEvent: (PersonalDataEvent) -> Unit,
    onError: (UIError) -> Unit,
    navController: NavController,
    navigationEvents: Flow<PersonalDataNavigationEvent>
) {
    ObserveAsNavigationEvents(flow = navigationEvents) { event ->
        when (event) {
            PersonalDataNavigationEvent.PopBackStack -> {
                navController.popBackStack()
            }
        }
    }

    val topBarBackButtonSize = 32.dp

    CheeseTopBarWrapper(topBarContent = {
        IconButton(
            modifier = Modifier
                .padding(start = CheeseTheme.paddings.smallest)
                .size(topBarBackButtonSize)
                .align(Alignment.CenterStart),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(id = R.string.personal_data_title),
            style = CheeseTheme.typography.common16Medium
        )
    }) {
        AnimatedContent(
            targetState = state,
            label = "Personal data animated content",
            transitionSpec = {
                fadeIn(tween(200)).togetherWith(fadeOut(tween(200)))
            },
            contentKey = { it.key }
        ) { uiState ->
            when (uiState) {
                is PersonalDataViewState.Error -> ErrorScreen(
                    error = uiState.error,
                    retry = { uiError ->
                        onError(uiError)
                    }
                )

                is PersonalDataViewState.Loading -> LoadingScreen()

                is PersonalDataViewState.Success -> PersonalDataContent(
                    state = uiState,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun PersonalDataContent(
    state: PersonalDataViewState.Success,
    onEvent: (PersonalDataEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val focus = LocalFocusManager.current

    val shakeController = rememberShakeController()
    val shakeConfig = ShakeConfig(
        iterations = 8,
        intensity = 100_000F,
        translateX = 5f
    )
    val confirm = {
        if (state.validation.allFieldsAreValid.not()) {
            vibrate(context)
            scope.launch {
                shakeController.shake(shakeConfig)
            }
        } else {
            focus.clearFocus()
            onEvent(PersonalDataEvent.Confirm)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
    ) {

        Spacer(modifier = Modifier.height(CheeseTheme.paddings.small))

        /*Title(stringResource(id = R.string.personal_data_title))*/

        PersonalDataTextField(
            title = stringResource(R.string.surname),
            value = state.surname,
            validationTextFieldResult = state.validation.surnameValidationResult,
            onValueChange = { surname ->
                onEvent(PersonalDataEvent.OnSurnameChange(surname))
            },
            focus = focus,
            shakeController = shakeController
        )
        PersonalDataTextField(
            title = stringResource(R.string.name),
            value = state.name,
            validationTextFieldResult = state.validation.nameValidationResult,
            onValueChange = { name ->
                onEvent(PersonalDataEvent.OnNameChange(name))
            },
            focus = focus,
            shakeController = shakeController
        )
        PersonalDataTextField(
            title = stringResource(R.string.patronymic),
            value = state.patronymic,
            validationTextFieldResult = state.validation.patronymicValidationResult,
            onValueChange = { patronymic ->
                onEvent(PersonalDataEvent.OnPatronymicChange(patronymic))
            },
            focus = focus,
            shakeController = shakeController
        )
        PersonalDataTextField(
            title = stringResource(R.string.birthday),
            value = state.birthday,
            onValueChange = { birthday ->
                onEvent(PersonalDataEvent.OnBirthdayChange(birthday))
            },
            focus = focus,
            shakeController = shakeController
        )
        PersonalDataTextField(
            title = stringResource(R.string.email),
            value = state.email,
            onValueChange = { email ->
                onEvent(PersonalDataEvent.OnEmailChange(email))
            },
            validationTextFieldResult = state.validation.emailValidationResult,
            focus = focus,
            shakeController = shakeController
        )
        PersonalDataTextField(
            title = stringResource(id = R.string.phone),
            value = state.phone,
            onValueChange = { phone ->
                if (phone.length == 10 && state.validation.allFieldsAreValid) focus.clearFocus()
                if (phone.length < 11) onEvent(PersonalDataEvent.OnPhoneChange(phone))
            },
            validationTextFieldResult = state.validation.phoneValidationResult,
            focus = focus,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    confirm()
                }
            ),
            visualTransformation = PhoneVisualTransformation("+7 (000) 000-00-00", '0'),
            shakeController = shakeController
        )

        CheeseButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            text = stringResource(R.string.save),
            onClick = { confirm() }
        )
    }
}


@Composable
private fun PersonalDataTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    validationTextFieldResult: ValidationTextFieldResult = ValidationTextFieldResult(),
    focus: FocusManager,
    keyboardActions: KeyboardActions = KeyboardActions(onDone = {
        focus.moveFocus(FocusDirection.Down)
    }),
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    shakeController: ShakeController
) {
    val modifier = if (validationTextFieldResult.success) Modifier
    else Modifier.shake(shakeController)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = CheeseTheme.typography.common12Light)
        CheeseTextField(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            validationTextFieldResult = validationTextFieldResult,
            visualTransformation = visualTransformation
        )
    }
}

fun vibrate(context: Context) {
    val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
    if (vibrator != null && vibrator.hasVibrator()) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                100,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }
}

@Composable
private fun Title(title: String) {
    Column {
        Text(
            text = title,
            style = CheeseTheme.typography.common20Bold
        )
    }
}