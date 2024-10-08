package ru.shum.domain.model

data class User(
    val name: String,
    val birthDate: String,
    val password: String,
    val registrationDate: Long,
    val avatarUrl: String?
)
