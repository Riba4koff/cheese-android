package ru.antares.cheese_android.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Paddings(
    val small: Dp = Dp.Unspecified,
    val medium: Dp = Dp.Unspecified,
    val large: Dp = Dp.Unspecified,
)

@Immutable
data class Colors(
    val accent: Color = Color.Unspecified,
    val black: Color = Color.Unspecified,
    val white: Color = Color.Unspecified,
    val primary: Color = Color.Unspecified,
    val bottomBarColor: Color = Color.Unspecified
)

val LocalColors =
    staticCompositionLocalOf { Colors() }

val LocalPaddings =
    staticCompositionLocalOf { Paddings() }

@Composable
fun CustomAndroidCheeseTheme(
    content: @Composable () -> Unit
) {
    val paddings = Paddings(
        small = 8.dp,
        medium = 16.dp,
        large = 32.dp,
    )

    val colors = Colors(
        accent = Color(0xFFFFDB87),
        black = Color.Black,
        white = Color.White,
        primary = Color(0xFFFFFFFF),
        bottomBarColor = Color.White
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.black.toArgb()
        }
    }

    CompositionLocalProvider(
        LocalPaddings provides paddings,
        LocalColors provides colors,
        content = content
    )
}

object CheeseTheme {
    internal val colors: Colors
        @Composable
        get() = LocalColors.current
    internal val paddings: Paddings
        @Composable
        get() = LocalPaddings.current
}