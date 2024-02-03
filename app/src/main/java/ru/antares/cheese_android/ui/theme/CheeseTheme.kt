package ru.antares.cheese_android.ui.theme

import android.app.Activity
import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val bottomBarColor: Color = Color.Unspecified,
    val gray: Color = Color.Unspecified,
    val red: Color = Color.Unspecified,
)

@Immutable
data class Shapes(
    val small: RoundedCornerShape = RoundedCornerShape(0),
    val medium: RoundedCornerShape = RoundedCornerShape(0),
    val large: RoundedCornerShape = RoundedCornerShape(0),
)

@Immutable
data class TextStyles(
    val common12Light: TextStyle = TextStyle.Default,
    val common14Light: TextStyle = TextStyle.Default,
    val common16Light: TextStyle = TextStyle.Default,
    val common18Light: TextStyle = TextStyle.Default,
    val common20Light: TextStyle = TextStyle.Default,
    val common22Light: TextStyle = TextStyle.Default,
    val common12Medium: TextStyle = TextStyle.Default,
    val common14Medium: TextStyle = TextStyle.Default,
    val common16Medium: TextStyle = TextStyle.Default,
    val common18Medium: TextStyle = TextStyle.Default,
    val common20Medium: TextStyle = TextStyle.Default,
    val common22Medium: TextStyle = TextStyle.Default,
    val common12Bold: TextStyle = TextStyle.Default,
    val common14Bold: TextStyle = TextStyle.Default,
    val common16Bold: TextStyle = TextStyle.Default,
    val common18Bold: TextStyle = TextStyle.Default,
    val common20Bold: TextStyle = TextStyle.Default,
    val common22Bold: TextStyle = TextStyle.Default,
)

val LocalColors =
    staticCompositionLocalOf { Colors() }

val LocalPaddings =
    staticCompositionLocalOf { Paddings() }

val LocalShapes =
    staticCompositionLocalOf { Shapes() }

val LocalTextStyles =
    staticCompositionLocalOf { TextStyles() }

@Composable
fun CheeseTheme(
    content: @Composable () -> Unit,
) {
    val paddings = Paddings(
        small = 8.dp,
        medium = 16.dp,
        large = 32.dp,
    )

    val shapes = Shapes(
        small = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp),
        medium = RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp),
        large = RoundedCornerShape(32.dp, 32.dp, 32.dp, 32.dp)
    )

    val colors = Colors(
        accent = Color(0xFFFFDB87),
        black = Color.Black,
        white = Color.White,
        primary = Color(0xFFFFFFFF),
        bottomBarColor = Color.White,
        gray = Color(0xFF999999),
        red = Color(0xFFEF0D1B),
    )

    val textStyles = TextStyles(
        common12Bold = TextStyle(fontSize = 12.sp, fontWeight = W700),
        common12Light = TextStyle(fontSize = 12.sp, fontWeight = W400),
        common12Medium = TextStyle(fontSize = 12.sp, fontWeight = W500),
        common14Bold = TextStyle(fontSize = 14.sp, fontWeight = W700),
        common14Light = TextStyle(fontSize = 14.sp, fontWeight = W400),
        common14Medium = TextStyle(fontSize = 14.sp, fontWeight = W500),
        common16Bold = TextStyle(fontSize = 16.sp, fontWeight = W700),
        common16Light = TextStyle(fontSize = 16.sp, fontWeight = W400),
        common16Medium = TextStyle(fontSize = 16.sp, fontWeight = W500),
        common18Bold = TextStyle(fontSize = 18.sp, fontWeight = W700),
        common18Light = TextStyle(fontSize = 18.sp, fontWeight = W400),
        common18Medium = TextStyle(fontSize = 18.sp, fontWeight = W500),
        common20Bold = TextStyle(fontSize = 20.sp, fontWeight = W700),
        common20Light = TextStyle(fontSize = 20.sp, fontWeight = W400),
        common20Medium = TextStyle(fontSize = 20.sp, fontWeight = W500),
        common22Bold = TextStyle(fontSize = 22.sp, fontWeight = W700),
        common22Light = TextStyle(fontSize = 22.sp, fontWeight = W400),
        common22Medium = TextStyle(fontSize = 22.sp, fontWeight = W500),
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
        LocalShapes provides shapes,
        LocalTextStyles provides textStyles,
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
    internal val shapes: Shapes
        @Composable
        get() = LocalShapes.current
    internal val textStyles: TextStyles
        @Composable
        get() = LocalTextStyles.current
}