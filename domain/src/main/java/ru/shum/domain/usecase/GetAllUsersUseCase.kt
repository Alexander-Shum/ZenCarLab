package ru.shum.domain.usecase

import ru.shum.domain.model.User

class GetAllUsersUseCase(
    private val userRepository: ru.shum.domain.repository.UserRepository
) {

    suspend operator fun invoke(userName: String): List<User> {
        return userRepository.getAllUsers(userName)
    }
}