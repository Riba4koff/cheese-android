package ru.antares.cheese_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.antares.cheese_android.ui.theme.CustomAndroidCheeseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomAndroidCheeseTheme {

            }
        }
    }
}