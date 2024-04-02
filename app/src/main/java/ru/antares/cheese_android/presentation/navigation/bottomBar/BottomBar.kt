@file:OptIn(ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.navigation.bottomBar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.mandatorySystemGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.antares.cheese_android.presentation.navigation.util.Screen
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun PreviewBottomBar() {
    CheeseTheme {
        BottomBar(navigate = {

        }, countInCart = 1, currentRoute = null)
    }
}

@Composable
fun BottomBar(
    navigate: (BottomBarDestination) -> Unit,
    countInCart: Int,
    currentRoute: String?
) {
    val heightBottomBar = 52.dp

    NavigationBar(
        modifier = Modifier.height(heightBottomBar),
        containerColor = CheeseTheme.colors.bottomBarColor,
        windowInsets = WindowInsets.mandatorySystemGestures
    ) {
        BottomBarRow {
            BottomBarDestination.entries.forEach { destination ->
                val (pressed, onPressedChange) = remember { mutableStateOf(false) }
                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.85f else 1f,
                    label = "Animation of bottom bar button"
                )

                BottomBarButtonBody(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                navigate(destination)
                            }, onPress = {
                                onPressedChange(true)
                                tryAwaitRelease()
                                onPressedChange(false)
                            })
                        }
                        .scale(scale)
                ) {
                    SelectedPageBackground(
                        destination = destination,
                        currentRoute = currentRoute
                    )
                    BottomBarButtonBadgedBox(
                        countInCart = countInCart,
                        destination = destination
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedPageBackground(
    destination: BottomBarDestination,
    currentRoute: String?,
) {
    val animateBackgroundOfBottomBarButtonBackground by animateColorAsState(
        targetValue = if (destination.route == currentRoute) CheeseTheme.colors.accent
        else CheeseTheme.colors.primary,
        label = "Animation of changing  of background bottom bar button"
    )
    val iconSize = 64.dp
    Box(
        modifier = Modifier
            .width(54.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(44.dp)
            .background(animateBackgroundOfBottomBarButtonBackground)
    ) {
        Icon(
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center),
            painter = painterResource(id = destination.icon),
            contentDescription = null,
            tint = CheeseTheme.colors.black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomBarButtonBadgedBox(
    destination: BottomBarDestination,
    countInCart: Int,
) {
    Box(
        modifier = Modifier
            .width(54.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(44.dp)
    ) {
        BadgedBox(
            modifier = Modifier.size(15.dp),
            badge = {
                if (destination.route == Screen.CartNavigationGraph.route && countInCart > 0) {
                    Badge(
                        modifier = Modifier
                            .offset((26).dp, 0.dp)
                            .align(Alignment.Center)
                    ) {
                        Text("$countInCart", fontSize = 10.sp)
                    }
                }
            }
        ) { /* NOTHING*/ }
    }
}

@Composable
private fun BottomBarRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        content()
    }
}

@Composable
private fun BottomBarButtonBody(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier) {
        content()
    }
}