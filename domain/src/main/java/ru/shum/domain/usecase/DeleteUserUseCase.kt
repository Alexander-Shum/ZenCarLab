package ru.shum.domain.usecase

import ru.shum.domain.repository.UserRepository

class DeleteUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(name: String) {
        userRepository.deleteUser(name)
    }
}