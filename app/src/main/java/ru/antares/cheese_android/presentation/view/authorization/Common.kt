package ru.antares.cheese_android.presentation.view.authorization

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun AgreementText(modifier: Modifier = Modifier, onAgreementClick: () -> Unit, onPrivacyPolicyClick: () -> Unit) {
    val grayColor = CheeseTheme.colors.gray
    val whiteColor = CheeseTheme.colors.white

    val clickableTextStyle =
        CheeseTheme.textStyles.common12Light.copy(color = whiteColor)
    val unClickableTextStyle =
        CheeseTheme.textStyles.common12Light.copy(color = grayColor)

    val (pressedAgreement, onAgreementPressedChange) = remember {
        mutableStateOf(false)
    }
    val (pressedPrivacyPolicy, onPrivacyPolicyPressedChange) = remember {
        mutableStateOf(false)
    }

    val scaleAgreement by animateFloatAsState(
        targetValue = if (pressedAgreement) 0.96f else 1f,
        label = "Animate size of text agreement",
    )

    val scalePrivacyPolicy by animateFloatAsState(
        targetValue = if (pressedPrivacyPolicy) 0.96f else 1f,
        label = "Animate size of text privacy policy",
    )

    Column(
        modifier = modifier.padding(bottom = CheeseTheme.paddings.medium),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = "Продолжая, я принимаю ",
                modifier = Modifier,
                style = unClickableTextStyle
            )
            Text(
                text = "пользовательское",
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        onAgreementPressedChange(true)
                        tryAwaitRelease()
                        onAgreementPressedChange(false)
                    }, onTap = {
                        onAgreementClick()
                    })
                }.scale(scaleAgreement),
                style = clickableTextStyle
            )
        }
        Row {
            Text(
                text = "соглашение",
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        onAgreementPressedChange(true)
                        tryAwaitRelease()
                        onAgreementPressedChange(false)
                    }, onTap = {
                        onAgreementClick()
                    })
                }.scale(scaleAgreement),
                style = clickableTextStyle
            )
            Text(
                text = ", и подтверждаю, что прочел",
                modifier = Modifier,
                style = unClickableTextStyle
            )
        }
        Row {
            Text(
                text = "политику конфиденциальности",
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        onPrivacyPolicyPressedChange(true)
                        tryAwaitRelease()
                        onPrivacyPolicyPressedChange(false)
                    }, onTap = {
                        onPrivacyPolicyClick()
                    })
                }.scale(scalePrivacyPolicy),
                style = clickableTextStyle
            )
        }
    }
}