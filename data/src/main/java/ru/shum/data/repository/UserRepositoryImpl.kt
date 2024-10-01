package ru.shum.data.repository

import ru.shum.data.local.db.user.UserDao
import ru.shum.data.local.db.user.model.toDomainModel
import ru.shum.data.local.db.user.model.toEntity
import ru.shum.domain.model.User
import ru.shum.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
): UserRepository {
    override suspend fun saveUser(user: User) {
        userDao.insert(user = user.toEntity())
    }

    override suspend fun loginUser(name: String, password: String): User? {
        return userDao.login(userName = name, password = password)?.toDomainModel()
    }

    override suspend fun isUserExists(name: String): Boolean {
        return userDao.isUserExists(userName = name) > 0
    }

    override suspend fun getUserByName(name: String): User? {
        return userDao.getUserByName(name)?.toDomainModel()
    }
}