package ru.antares.cheese_android

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute: String = destination.parent?.route ?: return koinViewModel()
    val parentEntry: NavBackStackEntry = remember(key1 = this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.onClick(onClick: () -> Unit) = Modifier.clickable(
    interactionSource = MutableInteractionSource(),
    indication = null,
    onClick = onClick
)