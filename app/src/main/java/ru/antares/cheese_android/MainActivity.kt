package ru.antares.cheese_android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.antares.cheese_android.data.remote.services.main.catalog.CatalogService
import ru.antares.cheese_android.data.repository.util.safeNetworkCall
import ru.antares.cheese_android.data.repository.util.safeNetworkCallWithPagination
import ru.antares.cheese_android.presentation.navigation.CheeseApp
import ru.antares.cheese_android.ui.theme.CheeseTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheeseTheme {
                val catalogService by inject<CatalogService>()
                val scope = rememberCoroutineScope()

                scope.launch {
                    safeNetworkCallWithPagination { catalogService.get() }.onFailure {
                        Log.d("RESPONSE_ERROR", it)
                    }.onSuccess { data ->
                        Log.d("RESPONSE_SUCCESS", data.toString())
                    }
                }

                val navController = rememberNavController()
                CheeseApp(globalNavController = navController)
            }
        }
    }
}