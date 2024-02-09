package ru.antares.cheese_android.presentation.components.shaker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun rememberShakeController(): ShakeController {  
    return remember { ShakeController() }  
}  
  
class ShakeController {  
    var shakeConfig: ShakeConfig? by mutableStateOf(null)
        private set
  
    suspend fun shake(shakeConfig: ShakeConfig?) {
        this.shakeConfig = shakeConfig
        delay(500)
        this.shakeConfig = null
    }  
}

