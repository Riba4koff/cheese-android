package ru.antares.cheese_android.presentation.components.wrappers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun CATopBarWrapper(
    topBarContent: @Composable BoxScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val topBarHeight = 42.dp
    Column {
        Box(
            modifier = Modifier
                .height(topBarHeight)
                .fillMaxWidth()
        ) {
            topBarContent()
        }
        Divider(color = CheeseTheme.colors.gray, modifier = Modifier.height(0.5.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CATopBarPreview() {
    CheeseTheme {
        CATopBarWrapper(
            topBarContent = {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = ""
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.personal_data_title),
                    style = CheeseTheme.textStyles.common16Medium
                )
            }
        ) {
            /* TODO: content */
        }
    }
}