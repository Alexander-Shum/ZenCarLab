package ru.shum.domain.usecase

import ru.shum.domain.repository.AuthRepository

class LoggedInUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}