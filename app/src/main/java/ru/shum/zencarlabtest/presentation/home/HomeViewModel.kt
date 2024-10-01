package ru.shum.zencarlabtest.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.shum.domain.model.User
import ru.shum.domain.usecase.DeleteUserUseCase
import ru.shum.domain.usecase.GetAllUsersUseCase
import ru.shum.domain.usecase.GetLoggedUserNameUseCase
import ru.shum.domain.usecase.GetUserByNameUseCase
import ru.shum.domain.usecase.LogoutUseCase

class HomeViewModel(
    private val getLoggedUserNameUseCase: GetLoggedUserNameUseCase,
    private val getUserByNameUseCase: GetUserByNameUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<HomeState>(HomeState.Loading)
    val state = _state.asStateFlow()

    init {
        getLoggedUser()
    }

    fun deleteUser(targetUser: User, showToast: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = getUserByNameUseCase(getLoggedUserNameUseCase.invoke() ?: "")

            if (currentUser != null) {
                if (targetUser.registrationDate > currentUser.registrationDate) {
                    deleteUserUseCase(targetUser.name)

                    val updatedUsers = getAllUsersUseCase(currentUser.name)
                    _state.value = HomeState.UserInfoScreen(
                        user = currentUser,
                        users = updatedUsers
                    )
                } else {
                    withContext(Dispatchers.Main){
                        showToast("You can't delete users with older registration date")
                    }
                }
            }
        }
    }


    fun getLoggedUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = getUserByNameUseCase(getLoggedUserNameUseCase.invoke() ?: "")

            if (user != null) {
                val users = getAllUsersUseCase(user.name)
                _state.value = HomeState.UserInfoScreen(
                    user = user,
                    users = users
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
        data class UserInfoScreen(val user: User, val users: List<User>? = null) : HomeState()
        data class Error(val message: String) : HomeState()
        object LoggedOut : HomeState()
        object Loading : HomeState()
    }
}