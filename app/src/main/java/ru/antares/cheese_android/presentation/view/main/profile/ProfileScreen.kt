package ru.antares.cheese_android.presentation.view.main.profile

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.antares.cheese_android.ObserveAsEvents
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.presentation.CheeseTitle
import ru.antares.cheese_android.presentation.LoadingScreen
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CheeseTheme {
        ProfileScreen(
            state = ProfileScreenState.LoadingState,
            navigationEvents = emptyFlow(),
            onEvent = { _ ->

            },
            onNavigationEvent = { _ ->

            },
            globalNavController = rememberNavController(),
            profileNavController = rememberNavController()
        )
    }
}

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    navigationEvents: Flow<ProfileNavigationEvent>,
    onEvent: (ProfileEvent) -> Unit,
    onNavigationEvent: (ProfileNavigationEvent) -> Unit,
    globalNavController: NavController,
    profileNavController: NavController
) {
    ObserveAsEvents(flow = navigationEvents) { event ->
        when (event) {
            ProfileNavigationEvent.Logout -> {
                globalNavController.navigate(Screen.AuthNavigationGraph.route) {
                    popUpTo(Screen.HomeNavigationGraph.route) {
                        inclusive = true
                    }
                }
            }

            ProfileNavigationEvent.NavigateToMyTickets -> {

            }

            ProfileNavigationEvent.NavigateToOrders -> {

            }

            ProfileNavigationEvent.NavigateToAboutApp -> {

            }

            ProfileNavigationEvent.NavigateToPersonalData -> {

            }

            ProfileNavigationEvent.NavigateToSavedAddresses -> {

            }

            ProfileNavigationEvent.Authorize -> {
                globalNavController.navigate(Screen.AuthNavigationGraph.route)
            }
        }
    }
    CheeseTitle(stringResource(id = R.string.profile_title)) {
        state.onLoadingState {
            LoadingScreen(Modifier.align(Alignment.Center))
        }.onErrorState { errorMessage ->
            ErrorScreen(
                modifier = Modifier.align(Alignment.Center),
                error = errorMessage,
                retry = { uiError ->
                    onEvent(ProfileEvent.Retry(uiError))
                }
            )
        }.onAuthorizedState { uiState ->
            UserIsAuthorizedContent(
                modifier = Modifier.align(Alignment.Center),
                onEvent = onEvent,
                onNavigationEvent = onNavigationEvent,
                state = uiState
            )
        }.onNonAuthorizedState {
            UserIsNotAuthorizedContent(
                modifier = Modifier.align(Alignment.Center),
                onNavigationEvent = onNavigationEvent
            )
        }
    }
}

@Composable
private fun UserIsAuthorizedContent(
    modifier: Modifier,
    state: ProfileScreenState.AuthorizedState,
    onEvent: (ProfileEvent) -> Unit,
    onNavigationEvent: (ProfileNavigationEvent) -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileHeader(
            state.surname ?: "Неизвестный",
            state.name ?: "Пользователь",
            state.patronymic ?: ""
        )
        ProfileFuture(onNavigationEvent = onNavigationEvent)
        ProfileBottom(onEvent = onEvent)
    }
}

@Composable
private fun ProfileHeader(
    surname: String, name: String, patronymic: String
) {
    val profileImageSize = 128.dp

    Column(
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium + CheeseTheme.paddings.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(profileImageSize),
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = null
        )
        Text(
            text = "$surname $name $patronymic", style = CheeseTheme.textStyles.common20Light
        )
    }
}

@Composable
private fun ProfileFuture(onNavigationEvent: (ProfileNavigationEvent) -> Unit) {
    Column(modifier = Modifier.padding(top = CheeseTheme.paddings.large)) {
        ProfileFutureItem(icon = R.drawable.personal_data_profile_icon,
            title = "Персональные данные",
            onClick = {
                onNavigationEvent(ProfileNavigationEvent.NavigateToPersonalData)
            }
        )
        ProfileFutureItem(icon = R.drawable.saved_addresses_profile_icon,
            title = "Сохраненные адреса",
            onClick = {
                onNavigationEvent(ProfileNavigationEvent.NavigateToSavedAddresses)
            }
        )
        ProfileFutureItem(icon = R.drawable.orders_history_profile_icon,
            title = "Мои заказы",
            onClick = {
                onNavigationEvent(ProfileNavigationEvent.NavigateToOrders)
            }
        )
        ProfileFutureItem(icon = R.drawable.my_tickets_profile_icon,
            title = "Мои билеты",
            onClick = {
                onNavigationEvent(ProfileNavigationEvent.NavigateToMyTickets)
            }
        )
        ProfileFutureItem(
            icon = R.drawable.about_app_profile_icon,
            title = "О приложении",
            onClick = {
                onNavigationEvent(ProfileNavigationEvent.NavigateToAboutApp)
            },
            visibleDivider = false
        )
    }
}

@Composable
private fun ProfileBottom(onEvent: (ProfileEvent) -> Unit) {
    val logoutButtonColors = ButtonDefaults.buttonColors(
        contentColor = CheeseTheme.colors.red,
        containerColor = Color.Transparent
    )
    val deleteAccountButtonColors = ButtonDefaults.buttonColors(
        contentColor = CheeseTheme.colors.gray,
        containerColor = Color.Transparent
    )

    Column(
        modifier = Modifier.padding(top = CheeseTheme.paddings.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.smallest)
    ) {
        TextButton(onClick = {
            onEvent(ProfileEvent.Logout)
        }, colors = logoutButtonColors, modifier = Modifier.height(32.dp)) {
            Text(
                stringResource(R.string.logout),
                style = CheeseTheme.textStyles.common14Medium
            )
        }
        /*TextButton(onClick = {
            onEvent(ProfileEvent.DeleteAccount)
        }, colors = deleteAccountButtonColors, modifier = Modifier.height(32.dp)) {
            Text(
                stringResource(R.string.delete_account),
                style = CheeseTheme.textStyles.common12Medium
            )
        }*/
    }
}

@Composable
private fun ProfileFutureItem(
    visibleDivider: Boolean = true,
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    val iconSize = 24.dp
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        label = "Animation profile future item"
    )

    Column(modifier = Modifier.padding(horizontal = CheeseTheme.paddings.medium)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CheeseTheme.paddings.smallest)
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = CheeseTheme.paddings.medium
                ),
                horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = CheeseTheme.colors.profileIconColor
                )
                Text(text = title, style = CheeseTheme.textStyles.common16Light)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = CheeseTheme.colors.profileIconColor
            )
        }
        if (visibleDivider) Divider(
            modifier = Modifier.height(0.5.dp),
            color = CheeseTheme.colors.gray
        )
    }
}

@Composable
private fun UserIsNotAuthorizedContent(
    modifier: Modifier, onNavigationEvent: (ProfileNavigationEvent) -> Unit
) {
    val profilePlaceholderImageSize = 128.dp
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = CheeseTheme.colors.accent, contentColor = CheeseTheme.colors.black
    )
    val buttonShape = CheeseTheme.shapes.medium

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(profilePlaceholderImageSize),
            painter = painterResource(id = R.drawable.profile_placeholder),
            contentDescription = "Profile placeholder"
        )
        Button(
            onClick = { onNavigationEvent(ProfileNavigationEvent.Authorize) },
            shape = buttonShape,
            colors = buttonColors
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = CheeseTheme.paddings.medium,
                    vertical = CheeseTheme.paddings.smallest
                ),
                text = stringResource(R.string.login),
                style = CheeseTheme.textStyles.common18Medium
            )
        }
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier,
    error: UIError,
    retry: (UIError) -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = error.message,
                style = CheeseTheme.textStyles.common14Medium,
                color = CheeseTheme.colors.gray
            )
            TextButton(onClick = { retry(error) }) {
                Text(
                    text = stringResource(R.string.retry),
                    style = CheeseTheme.textStyles.common14Medium,
                    color = CheeseTheme.colors.blue
                )
            }
        }
    }
}
