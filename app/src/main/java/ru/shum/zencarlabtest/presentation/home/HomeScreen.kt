package ru.shum.zencarlabtest.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import ru.shum.domain.model.User
import ru.shum.zencarlabtest.R
import ru.shum.zencarlabtest.navigation.Screen
import ru.shum.zencarlabtest.presentation.components.ErrorScreen
import ru.shum.zencarlabtest.presentation.components.LoadingScreen


@Composable
fun HomeScreen(
    navHostController: NavHostController
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    when (state) {
        HomeViewModel.HomeState.Loading -> {
            LoadingScreen()
        }

        is HomeViewModel.HomeState.UserInfoScreen -> {
            val userInfoState = state as HomeViewModel.HomeState.UserInfoScreen
            UserInfoScreen(
                user = userInfoState.user,
                logout = {
                    viewModel.logout()
                }
            )
        }


        is HomeViewModel.HomeState.Error -> {
            ErrorScreen(
                errorMessage = (state as HomeViewModel.HomeState.Error).message,
                onRetry = {

                }
            )
        }

        HomeViewModel.HomeState.LoggedOut -> {
            navHostController.navigate(Screen.Auth) {
                popUpTo(Screen.Auth) { inclusive = true }
            }
            viewModel.getLoggedUser()
        }
    }
}

@Composable
fun UserInfoScreen(
    user: User,
    logout: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .padding(
                        top = WindowInsets.systemBars
                            .asPaddingValues()
                            .calculateTopPadding()
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    text = stringResource(R.string.data_title),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .align(Alignment.TopEnd),
                    onClick = {
                        logout()
                    }
                ) {
                    Text(stringResource(R.string.logout))
                }

            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .padding(paddingValues)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "${stringResource(R.string.name)}: ${user.name}"
                )

                Text(
                    text = stringResource(
                        R.string.birthday, user.birthDate.replace(
                            Regex("(\\d{2})(\\d{2})(\\d{4})"),
                            "$1/$2/$3"
                        )
                    )
                )
            }
        }
    )
}