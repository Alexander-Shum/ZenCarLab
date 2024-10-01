package ru.shum.zencarlabtest.presentation.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.shum.domain.model.User
import ru.shum.domain.usecase.LoggedInUseCase
import ru.shum.domain.usecase.LoginUserUseCase
import ru.shum.domain.usecase.SaveUserUseCase
import ru.shum.domain.usecase.UserExistsUseCase

class AuthViewModel(
    private val saveUserUseCase: SaveUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val userExistsUseCase: UserExistsUseCase,
    private val loggedInUseCase: LoggedInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.RegistrationScreen)
    val state = _state.asStateFlow()

    init {
        if (loggedInUseCase.invoke()) {
            _state.value = AuthState.LoggedIn
        } else {
            _state.value = AuthState.RegistrationScreen
        }
    }

    fun switchToRegistration() {
        _state.value = AuthState.RegistrationScreen
    }

    fun switchToLogin() {
        _state.value = AuthState.LoginScreen
    }

    fun registerUser(name: String, birthDate: String, password: String, avatarUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userExistsUseCase(name)) {
                _state.value = AuthState.Error("User already exists")
                return@launch
            }
            try {
                val user = User(
                    name = name, birthDate = birthDate,
                    password = password,
                    registrationDate = System.currentTimeMillis(),
                    avatarUrl = avatarUri?.toString()
                )
                saveUserUseCase(user)
                _state.value = AuthState.LoginScreen
            } catch (e: Exception) {
                _state.value = AuthState.Error("Registration failed")
            }
        }
    }

    fun loginUser(name: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = loginUserUseCase(name, password)
                if (user != null) {
                    _state.value = AuthState.LoggedIn
                } else {
                    _state.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _state.value = AuthState.Error("Login failed")
            }
        }
    }

    sealed class AuthState {
        object Loading : AuthState()
        object RegistrationScreen : AuthState()
        object LoginScreen : AuthState()
        object LoggedIn : AuthState()
        data class Error(val message: String) : AuthState()
    }
}