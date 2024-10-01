package ru.shum.domain.usecase

import ru.shum.domain.repository.UserRepository

class UserExistsUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String): Boolean {
        return userRepository.isUserExists(name)
    }
}