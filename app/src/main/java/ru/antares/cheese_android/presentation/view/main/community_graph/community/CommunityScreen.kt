@file:OptIn(ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.community_graph.community

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.antares.cheese_android.R
import ru.antares.cheese_android.presentation.components.screens.LoadingScreen
import ru.antares.cheese_android.presentation.components.topbars.CheeseTopAppBar
import ru.antares.cheese_android.presentation.components.wrappers.CheeseTitleWrapper
import ru.antares.cheese_android.ui.theme.CheeseTheme

@Preview(showBackground = true)
@Composable
fun CommunityScreenPreview() {
    CheeseTheme {
        CommunityScreen(
            onEvent = {},
            onNavigationEvent = {},
            state = CommunityScreenState(
                posts = emptyList()
            )
        )
    }
}

@Composable
fun CommunityScreen(
    onEvent: (CommunityEvent) -> Unit,
    onNavigationEvent: (CommunityNavigationEvent) -> Unit,
    state: CommunityScreenState
) {
    CheeseTopAppBar(stringResource(id = R.string.community_title)) {
        AnimatedContent(
            targetState = state.loading,
            label = "Catalog screen animated content",
            transitionSpec = { fadeIn(tween(200)).togetherWith(fadeOut(tween(200))) },
        ) { loading ->
            if (loading) {
                LoadingScreen()
            } else {
                Column(
                    modifier = Modifier
                        .padding(CheeseTheme.paddings.medium),
                    verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
                ) {
                    state.posts?.forEach { post ->
                        Text(text = post.title)
                    }
                }
            }
        }
    }
}
