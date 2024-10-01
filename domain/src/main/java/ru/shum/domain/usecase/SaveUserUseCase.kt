package ru.shum.domain.usecase

import ru.shum.domain.model.User
import ru.shum.domain.repository.UserRepository

class SaveUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User) = userRepository.saveUser(user = user)
}