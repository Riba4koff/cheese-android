package ru.antares.cheese_android.presentation.navigation.navhosts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.antares.cheese_android.presentation.navigation.graphs.cartNavigationGraph
import ru.antares.cheese_android.presentation.navigation.util.Screen

@Composable
fun CartNavHost(
    paddings: PaddingValues,
    cartNavController: NavHostController
) {
    NavHost(
        modifier = Modifier.padding(paddings),
        navController = cartNavController,
        startDestination = Screen.CartNavigationGraph.route
    ) {
        cartNavigationGraph(cartNavController)
    }
}