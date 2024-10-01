package ru.shum.domain.repository

import ru.shum.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User)

    suspend fun loginUser(name: String, password: String): User?

    suspend fun isUserExists(name: String): Boolean

    suspend fun getUserByName(name: String): User?

    suspend fun getAllUsers(name: String): List<User>

    suspend fun deleteUser(name: String)
}