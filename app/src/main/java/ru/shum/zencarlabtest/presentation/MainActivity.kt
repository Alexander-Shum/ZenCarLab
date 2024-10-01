package ru.shum.zencarlabtest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.shum.zencarlabtest.navigation.AppNavHost
import ru.shum.zencarlabtest.presentation.theme.ZenCarLabTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenCarLabTestTheme {
                AppNavHost()
            }
        }
    }
}