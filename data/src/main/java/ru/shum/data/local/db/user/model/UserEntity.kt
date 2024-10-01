package ru.shum.data.local.db.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.shum.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val birthDate: String,
    val password: String,
    val registrationDate: Long,
    val avatarUrl: String? = null
)

fun UserEntity.toDomainModel() = User (
    name = name,
    birthDate = birthDate,
    password = password,
    registrationDate = registrationDate,
    avatarUrl = avatarUrl
)

fun User.toEntity() = UserEntity (
    name = name,
    birthDate = birthDate,
    password = password,
    registrationDate = registrationDate,
    avatarUrl = avatarUrl
)
