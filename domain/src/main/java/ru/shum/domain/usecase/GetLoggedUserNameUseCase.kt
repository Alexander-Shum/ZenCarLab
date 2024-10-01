package ru.shum.domain.usecase

import ru.shum.domain.repository.AuthRepository

class GetLoggedUserNameUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String? {
        return authRepository.getLoggedInUserName()
    }
}