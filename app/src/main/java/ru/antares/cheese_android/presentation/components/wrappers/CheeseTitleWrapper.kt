package ru.antares.cheese_android.presentation.components.wrappers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.textfields.CheeseSearchTextField
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Composable
fun CheeseTitleWrapper(
    title: String,
    searchValue: String = "",
    onSearchChange: ((String) -> Unit)? = null,
    onSearch: ((String) -> Unit) = {},
    content: @Composable BoxScope.() -> Unit
) {
    Column {
        Column(
            modifier = Modifier
                .padding(CheeseTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
        ) {
            Text(
                text = title,
                style = CheeseTheme.textStyles.largeTitle
            )
            onSearchChange?.let {
                CheeseSearchTextField(
                    modifier = Modifier.fillMaxWidth(),
                    search = onSearch,
                    value = searchValue,
                    onValueChange = it
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheeseTitleWrapperPreview() {
    CheeseTheme {
        CheeseTitleWrapper(
            searchValue = "",
            onSearchChange = { search ->

            },
            title = stringResource(id = R.string.home_title)
        ) {
            /*TODO: content*/
        }
    }
}