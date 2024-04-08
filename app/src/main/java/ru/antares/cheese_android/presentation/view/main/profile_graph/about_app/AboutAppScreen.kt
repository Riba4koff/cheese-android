@file:OptIn(ExperimentalMaterial3Api::class)

package ru.antares.cheese_android.presentation.view.main.profile_graph.about_app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.antares.cheese_android.R
import ru.antares.cheese_android.clickable
import ru.antares.cheese_android.ui.theme.CheeseTheme

/**
 * @author pavelrybakov
 * Created 08.04.2024 at 17:09
 * Android Studio
 */

@Preview
@Composable
private fun AboutAppScreenPreview() {
    CheeseTheme {
        AboutAppScreen(
            navController = rememberNavController()
        )
    }
}

@Composable
fun AboutAppScreen(
    navController: NavController,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState()
    )
    val packageManager = LocalContext.current.packageManager
    val info = packageManager.getPackageInfo(LocalContext.current.packageName, 0)
    val versionName = info.versionName

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.about_app_title),
                        style = CheeseTheme.typography.common16Semibold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CheeseTheme.colors.white
                )
            )
        },
        containerColor = CheeseTheme.colors.white
    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = CheeseTheme.paddings.large),
                verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.large)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.about_app_icon),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.mesie_rokfor),
                        style = CheeseTheme.typography.common30Bold
                    )
                    Text(
                        text = "Версия приложения $versionName",
                        style = CheeseTheme.typography.common12Regular,
                        color = CheeseTheme.colors.gray
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
                ) {
                    AboutAppTab(stringResource(R.string.terms_of_use)) { uriHandler.openUri("https://mrokfor.ru/policy") }
                    AboutAppTab(stringResource(R.string.privacy_policy)) { uriHandler.openUri("https://mrokfor.ru/agreement") }
                }
            }
        }
    }
}

@Composable
private fun AboutAppTab(
    title: String,
    onClick: () -> Unit
) {
    val (pressed, onPressedChange) = remember { mutableStateOf(false) }
    val animatedScale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        label = "About app pressed animated scale"
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (pressed) 0.5f else 1f,
        label = "About app pressed animated alpha"
    )

    Column(
        modifier = Modifier
            .clickable(
                scale = animatedScale,
                onPressedChange = onPressedChange,
                onClick = onClick
            )
            .alpha(animatedAlpha),
        verticalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(CheeseTheme.paddings.small)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.doc_icon),
                    contentDescription = null
                )
                Text(text = title)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CheeseTheme.paddings.medium)
        )
    }
}