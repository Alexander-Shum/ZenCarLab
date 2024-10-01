package ru.shum.domain.usecase

import ru.shum.domain.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository
) {

    operator fun invoke() = authRepository.logout()
}