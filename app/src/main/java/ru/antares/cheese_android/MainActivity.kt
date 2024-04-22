package ru.antares.cheese_android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import ru.antares.cheese_android.presentation.navigation.CheeseApp
import ru.antares.cheese_android.ui.theme.CheeseTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheeseTheme {
                val navController = rememberNavController()
                CheeseApp(globalNavController = navController)
            }
        }
    }
}