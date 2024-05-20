package ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.create

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.buttons.CheeseButton
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.textfields.CheeseTextField
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTopBarWrapper
import ru.antares.cheese_android.presentation.view.main.profile_graph.addresses.AddressesNavigationEvent
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 17.05.2024 at 17:41
 * Android Studio
 */

@Preview(showBackground = true)
@Composable
private fun CreateAddressScreenPreview() {
    CheeseTheme {
        CreateAddressScreen(
            state = CreateAddressScreenState(),
            onEvent = {},
            onNavigationEvent = {}
        )
    }
}

@Composable
fun CreateAddressScreen(
    state: CreateAddressScreenState,
    onEvent: (CreateAddressEvent) -> Unit,
    onNavigationEvent: (CreateAddressNavigationEvent) -> Unit
) {
    CheeseTopBarWrapper(
        topBarContent = {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(start = CheeseTheme.paddings.smallest)
                        .size(CheeseTheme.paddings.large),
                    onClick = {
                        onNavigationEvent(CreateAddressNavigationEvent.NavigateBack)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(id = R.string.back),
                    style = CheeseTheme.typography.common16Medium
                )
            }
        }
    ) {
        AnimatedContent(targetState = state.loading, label = "Loading create address", transitionSpec = {
            fadeIn(tween(256)).togetherWith(fadeOut(tween(256)))
        }) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                CreateAddressScreenContent(
                    state = state,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun CreateAddressScreenContent(
    state: CreateAddressScreenState,
    onEvent: (CreateAddressEvent) -> Unit
) {
    val textFieldHorizontalPadding = CheeseTheme.paddings.medium
    val verticalSpacing = CheeseTheme.paddings.medium
    val focus = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Город",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                modifier = Modifier,
                value = state.city,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnCityChange(it))
                },
                placeholder = "Введите город",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                validationTextFieldResult = state.validation.cityValidation
            )
        }
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Улица",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                value = state.street,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnStreetChange(it))
                },
                placeholder = "Введите улицу",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                validationTextFieldResult = state.validation.streetValidation
            )
        }
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Дом",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                value = state.house,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnHouseChange(it))
                },
                placeholder = "Введите дом",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                validationTextFieldResult = state.validation.houseValidation
            )
        }
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Корпус (Необязательно)",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                value = state.building,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnBuildingChange(it))
                },
                placeholder = "Введите корпус",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
        }
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Подъезд (Необязательно)",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                value = state.entrance,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnEntranceChange(it))
                },
                placeholder = "Введите подъезд",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
        }
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Этаж (Необязательно)",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                value = state.floor,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnFloorChange(it))
                },
                placeholder = "Введите этаж",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
        }
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Квартира (Необязательно)",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                value = state.apartment,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnApartmentChange(it))
                },
                placeholder = "Введите квартиру",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focus.moveFocus(FocusDirection.Down)
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            )
        }
        Column(
            Modifier.padding(horizontal = textFieldHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
        ) {
            Text(
                text = "Комментарий курьеру",
                style = CheeseTheme.typography.common12Regular,
                color = CheeseTheme.colors.gray
            )
            CheeseTextField(
                value = state.comment,
                onValueChange = {
                    onEvent(CreateAddressEvent.OnCommentChange(it))
                },
                placeholder = "Введите комментарий курьеру",
                enabled = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focus.clearFocus()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                validationTextFieldResult = state.validation.commentValidation
            )
        }
        CheeseButton(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .padding(horizontal = CheeseTheme.paddings.medium),
            text = "Сохранить",
        ) {
            onEvent(
                CreateAddressEvent.OnSaveClick(
                    city = state.city,
                    street = state.street,
                    house = state.house,
                    building = state.building,
                    entrance = state.entrance,
                    floor = state.floor,
                    apartment = state.apartment,
                    comment = state.comment
                )
            )
        }
    }
}