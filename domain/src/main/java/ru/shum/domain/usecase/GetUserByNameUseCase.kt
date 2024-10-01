package ru.shum.domain.usecase

import ru.shum.domain.model.User
import ru.shum.domain.repository.UserRepository

class GetUserByNameUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(name: String): User? {
        return userRepository.getUserByName(name)
    }
}