package ru.shum.zencarlabtest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.shum.zencarlabtest.presentation.auth.AuthScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import ru.shum.zencarlabtest.presentation.home.HomeScreen

sealed class Screen {
    @Serializable
    object Auth

    @Serializable
    object Home
}

@Composable
fun AppNavHost(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(navController = navHostController, startDestination = Screen.Auth) {
        composable<Screen.Auth> {
            AuthScreen(
                navHostController = navHostController
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                navHostController = navHostController
            )
        }
    }
}