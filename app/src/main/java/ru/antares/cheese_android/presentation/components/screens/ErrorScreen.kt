package ru.antares.cheese_android.presentation.components.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.antares.cheese_android.R
import ru.antares.cheese_android.domain.errors.UIError
import ru.antares.cheese_android.presentation.view.main.profile_graph.personal_data.PersonalDataUIError
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    error: UIError,
    retry: (UIError) -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = error.message,
                style = CheeseTheme.typography.common12Light,
                color = CheeseTheme.colors.gray
            )
            TextButton(modifier = Modifier.height(32.dp), onClick = { retry(error) }) {
                Text(
                    text = stringResource(R.string.retry),
                    style = CheeseTheme.typography.common14Light,
                    color = CheeseTheme.colors.blue
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    CheeseTheme {
        ErrorScreen(modifier = Modifier,
            error = PersonalDataUIError.SomeError("Хз"), retry = {})
    }
}