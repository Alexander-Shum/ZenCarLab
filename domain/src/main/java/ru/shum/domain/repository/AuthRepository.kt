package ru.shum.domain.repository

interface AuthRepository {
    fun isLoggedIn(): Boolean
    fun getLoggedInUserName(): String?
    fun login(userName: String)
    fun logout()
}