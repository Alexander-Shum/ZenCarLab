package ru.shum.zencarlabtest.presentation.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import ru.shum.zencarlabtest.R
import ru.shum.zencarlabtest.navigation.Screen
import ru.shum.zencarlabtest.presentation.components.ErrorScreen
import ru.shum.zencarlabtest.presentation.components.LoadingScreen
import ru.shum.zencarlabtest.util.DateTransformation
import ru.shum.zencarlabtest.util.isBirthDateValid
import ru.shum.zencarlabtest.util.isNameValid
import ru.shum.zencarlabtest.util.isPasswordValid

const val DATE_LENGTH = 8

@Composable
fun AuthScreen(
    navHostController: NavHostController
) {
    val viewModel = koinViewModel<AuthViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state == AuthViewModel.AuthState.LoggedIn) {
            navHostController.navigate(Screen.Home) {
                popUpTo(Screen.Home) { inclusive = true }
            }
        }
    }

    when (state) {
        AuthViewModel.AuthState.RegistrationScreen -> {
            RegistrationForm(
                context = context,
                onRegister = { name, birthDate, password, avatarUri ->
                    viewModel.registerUser(name = name, birthDate = birthDate, password = password, avatarUri = avatarUri)
                },
                onSwitchToLogin = {
                    viewModel.switchToLogin()
                }
            )
        }

        AuthViewModel.AuthState.LoginScreen -> {
            LoginForm(
                onLogin = { name, password ->
                    viewModel.loginUser(name = name, password = password)
                },
                onSwitchToRegistration = {
                    viewModel.switchToRegistration()
                }
            )
        }

        is AuthViewModel.AuthState.Error -> {
            ErrorScreen(
                errorMessage = (state as AuthViewModel.AuthState.Error).message,
                onRetry = {
                    viewModel.switchToLogin()
                })
        }

        AuthViewModel.AuthState.Loading -> {
            LoadingScreen()
        }

        else -> {

        }
    }
}

@Composable
fun LoginForm(onLogin: (String, String) -> Unit, onSwitchToRegistration: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.login), fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            onLogin(name, password)
        }) {
            Text(stringResource(R.string.login))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onSwitchToRegistration) {
            Text(stringResource(R.string.switch_to_registration))
        }
    }
}

@Composable
fun RegistrationForm(
    context: Context,
    onRegister: (String, String, String, Uri?) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var validationErrorMessage by remember { mutableStateOf<String?>(null) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            avatarUri = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.registration),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = birthDate,
            onValueChange = { input ->
                if (input.length <= DATE_LENGTH) {
                    birthDate = input
                }
            },
            label = { Text(stringResource(R.string.birth_date_dd_mm_yyyy)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            visualTransformation = DateTransformation(),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            })
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            imagePickerLauncher.launch(PickVisualMediaRequest(
                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
            ))
        }) {
            Text(text = stringResource(R.string.select_avatar))
        }

        Spacer(modifier = Modifier.height(8.dp))

        avatarUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(100.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }

        if (validationErrorMessage != null) {
            Text(
                text = validationErrorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            validationErrorMessage = when {
                !isNameValid(name) -> context.getString(R.string.name_cannot_be_empty)
                !isBirthDateValid(birthDate) -> context.getString(R.string.invalid_birth_date_format)
                !isPasswordValid(password) -> context.getString(R.string.password_must_be_at_least_6)
                else -> {
                    onRegister(name, birthDate, password, avatarUri)
                    null
                }
            }
        }) {
            Text(stringResource(R.string.register))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onSwitchToLogin) {
            Text(stringResource(R.string.switch_to_login))
        }
    }
}


