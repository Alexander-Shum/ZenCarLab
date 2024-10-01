package ru.shum.zencarlabtest.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.shum.domain.model.User
import ru.shum.domain.usecase.GetLoggedUserNameUseCase
import ru.shum.domain.usecase.GetUserByNameUseCase
import ru.shum.domain.usecase.LogoutUseCase

class HomeViewModel(
    private val getLoggedUserNameUseCase: GetLoggedUserNameUseCase,
    private val getUserByNameUseCase: GetUserByNameUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state = _state.asStateFlow()

    init {
        getLoggedUser()
    }

    fun getLoggedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = getUserByNameUseCase(getLoggedUserNameUseCase.invoke() ?: "")

            if (user != null) {
                _state.value = HomeState.UserInfoScreen(
                    user = user
                )
            } else {
                _state.value = HomeState.Error(
                    message = "User not found"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase()
            _state.value = HomeState.LoggedOut
        }
    }

    sealed class HomeState {
        data class UserInfoScreen(val user: User) : HomeState()
        data class Error(val message: String) : HomeState()
        object LoggedOut : HomeState()
        object Loading : HomeState()
    }
}