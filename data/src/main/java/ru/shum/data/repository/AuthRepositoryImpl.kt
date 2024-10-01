package ru.shum.data.repository

import ru.shum.data.local.sharedpref.AuthManager
import ru.shum.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authManager: AuthManager
): AuthRepository {
    override fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn()
    }

    override fun getLoggedInUserName(): String? {
        return authManager.getLoggedInUserName()
    }

    override fun login(userName: String) {
        authManager.login(userName)
    }

    override fun logout() {
        authManager.logout()
    }
}