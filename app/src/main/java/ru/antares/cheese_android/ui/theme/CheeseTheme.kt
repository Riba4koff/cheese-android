package ru.antares.cheese_android.ui.theme

import android.app.Activity
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class Paddings(
    val smallest: Dp = Dp.Unspecified,
    val small: Dp = Dp.Unspecified,
    val medium: Dp = Dp.Unspecified,
    val large: Dp = Dp.Unspecified
)

@Immutable
data class Colors(
    val accent: Color = Color.Unspecified,
    val black: Color = Color.Unspecified,
    val white: Color = Color.Unspecified,
    val primary: Color = Color.Unspecified,
    val bottomBarColor: Color = Color.Unspecified,
    val gray: Color = Color.Unspecified,
    val lightGray: Color = Color.Unspecified,
    val red: Color = Color.Unspecified,
    val blue: Color = Color.Unspecified,
    val profileIconColor: Color = Color.Unspecified
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
    val common24Light: TextStyle = TextStyle.Default,
    val common26Light: TextStyle = TextStyle.Default,
    val common28Light: TextStyle = TextStyle.Default,
    val common30Light: TextStyle = TextStyle.Default,
    val common12Regular: TextStyle = TextStyle.Default,
    val common14Regular: TextStyle = TextStyle.Default,
    val common16Regular: TextStyle = TextStyle.Default,
    val common18Regular: TextStyle = TextStyle.Default,
    val common20Regular: TextStyle = TextStyle.Default,
    val common22Regular: TextStyle = TextStyle.Default,
    val common24Regular: TextStyle = TextStyle.Default,
    val common26Regular: TextStyle = TextStyle.Default,
    val common28Regular: TextStyle = TextStyle.Default,
    val common30Regular: TextStyle = TextStyle.Default,
    val common12Medium: TextStyle = TextStyle.Default,
    val common14Medium: TextStyle = TextStyle.Default,
    val common16Medium: TextStyle = TextStyle.Default,
    val common18Medium: TextStyle = TextStyle.Default,
    val common20Medium: TextStyle = TextStyle.Default,
    val common22Medium: TextStyle = TextStyle.Default,
    val common24Medium: TextStyle = TextStyle.Default,
    val common26Medium: TextStyle = TextStyle.Default,
    val common28Medium: TextStyle = TextStyle.Default,
    val common30Medium: TextStyle = TextStyle.Default,
    val common12Semibold: TextStyle = TextStyle.Default,
    val common14Semibold: TextStyle = TextStyle.Default,
    val common16Semibold: TextStyle = TextStyle.Default,
    val common18Semibold: TextStyle = TextStyle.Default,
    val common20Semibold: TextStyle = TextStyle.Default,
    val common22Semibold: TextStyle = TextStyle.Default,
    val common24Semibold: TextStyle = TextStyle.Default,
    val common26Semibold: TextStyle = TextStyle.Default,
    val common28Semibold: TextStyle = TextStyle.Default,
    val common30Semibold: TextStyle = TextStyle.Default,
    val common12Bold: TextStyle = TextStyle.Default,
    val common14Bold: TextStyle = TextStyle.Default,
    val common16Bold: TextStyle = TextStyle.Default,
    val common18Bold: TextStyle = TextStyle.Default,
    val common20Bold: TextStyle = TextStyle.Default,
    val common22Bold: TextStyle = TextStyle.Default,
    val common24Bold: TextStyle = TextStyle.Default,
    val common26Bold: TextStyle = TextStyle.Default,
    val common28Bold: TextStyle = TextStyle.Default,
    val common30Bold: TextStyle = TextStyle.Default,
    val largeTitle: TextStyle = TextStyle.Default
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
        smallest = 4.dp,
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
        lightGray = Color(0xFFEFEFEF),
        red = Color(0xFFEF0D1B),
        blue = Color(0xFF007CF9),
        profileIconColor = Color(0xFF6C757D)
    )

    val textStyles = TextStyles(
        common12Light = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Light),
        common14Light = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Light),
        common16Light = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Light),
        common18Light = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Light),
        common20Light = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Light),
        common22Light = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Light),
        common24Light = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Light),
        common26Light = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Light),
        common28Light = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Light),
        common30Light = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Light),
        common12Regular = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
        common14Regular = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
        common16Regular = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
        common18Regular = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal),
        common20Regular = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal),
        common22Regular = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Normal),
        common24Regular = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Normal),
        common26Regular = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Normal),
        common28Regular = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Normal),
        common30Regular = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Normal),
        common12Medium = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
        common14Medium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
        common16Medium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
        common18Medium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        common20Medium = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
        common22Medium = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium),
        common24Medium = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Medium),
        common26Medium = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Medium),
        common28Medium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Medium),
        common30Medium = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Medium),
        common12Semibold = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        common14Semibold = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
        common16Semibold = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        common18Semibold = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
        common20Semibold = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
        common22Semibold = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
        common24Semibold = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
        common26Semibold = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.SemiBold),
        common28Semibold = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold),
        common30Semibold = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold),
        common12Bold = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
        common14Bold = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
        common16Bold = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
        common18Bold = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        common20Bold = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        common22Bold = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
        common24Bold = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
        common26Bold = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold),
        common28Bold = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
        common30Bold = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
        largeTitle = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Bold)
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
    internal val typography: TextStyles
        @Composable
        get() = LocalTextStyles.current
}