package ru.antares.cheese_android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import ru.antares.cheese_android.presentation.navigation.CheeseApp
import ru.antares.cheese_android.ui.theme.CustomAndroidCheeseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomAndroidCheeseTheme {
                val navController = rememberNavController()
                CheeseApp(navController = navController)
            }
        }
    }
}