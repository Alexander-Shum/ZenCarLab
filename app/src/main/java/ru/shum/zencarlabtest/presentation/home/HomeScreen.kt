package ru.shum.zencarlabtest.presentation.home


import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.shum.domain.model.User
import ru.shum.zencarlabtest.R
import ru.shum.zencarlabtest.navigation.Screen
import ru.shum.zencarlabtest.presentation.components.ErrorScreen
import ru.shum.zencarlabtest.presentation.components.LoadingScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_FORMAT = "dd MMMM yyyy, HH:mm:ss"

@Composable
fun HomeScreen(
    navHostController: NavHostController
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    when (state) {
        HomeViewModel.HomeState.Loading -> {
            LoadingScreen()
        }

        is HomeViewModel.HomeState.UserInfoScreen -> {
            val userInfoState = state as HomeViewModel.HomeState.UserInfoScreen
            UserInfoScreen(
                user = userInfoState.user,
                users = userInfoState.users,
                logout = {
                    viewModel.logout()
                },
                deleteUser = { userToDelete ->
                    viewModel.deleteUser(userToDelete) { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
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
    users: List<User>?,
    logout: () -> Unit,
    deleteUser: (User) -> Unit
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
                    fontWeight = FontWeight.Bold,
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
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                item {
                    UserItem(user = user, isMe = true)
                }

                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(bottom = 10.dp)
                            .fillMaxWidth(),
                        text = stringResource(R.string.all_users),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                if (!users.isNullOrEmpty()) {
                    itemsIndexed(users) { _, item ->
                        UserItem(item, deleteUser = deleteUser)
                    }
                } else {
                    item {
                        Text(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .padding(bottom = 10.dp)
                                .fillMaxWidth(),
                            text = stringResource(R.string.no_users),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun UserItem(
    user: User,
    isMe: Boolean = false,
    deleteUser: (User) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp),
    ) {
        user.avatarUrl?.let { avatarUri ->

            AsyncImage(
                model = Uri.parse(avatarUri),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

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

        Text(
            text = stringResource(
                R.string.date_of_registration,
                formatTimestampToDate(user.registrationDate)
            )
        )

        if (!isMe) {
            Button(modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(), onClick = {
                deleteUser(user)
            }) {
                Text(stringResource(R.string.delete))
            }

            HorizontalDivider(
                Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

private fun formatTimestampToDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    val date = Date(timestamp)

    return dateFormat.format(date)
}