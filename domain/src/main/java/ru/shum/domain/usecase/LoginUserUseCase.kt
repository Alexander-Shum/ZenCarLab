package ru.shum.domain.usecase

import ru.shum.domain.model.User
import ru.shum.domain.repository.AuthRepository
import ru.shum.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(userName: String, password: String): User? {
        authRepository.login(userName)
        return userRepository.loginUser(userName, password)
    }
}

