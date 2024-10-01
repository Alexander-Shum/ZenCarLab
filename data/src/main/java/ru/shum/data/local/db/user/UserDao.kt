package ru.shum.data.local.db.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.shum.data.local.db.user.model.UserEntity

@Dao
interface UserDao {
    @Insert
    fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE name = :userName LIMIT 1")
    suspend fun getUserByName(userName: String): UserEntity?

    @Query("SELECT COUNT(*) FROM users WHERE name = :userName")
    suspend fun isUserExists(userName: String): Int

    @Query("SELECT * FROM users WHERE name = :userName AND password = :password LIMIT 1")
    suspend fun login(userName: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE name != :userName ORDER BY registrationDate")
    fun getAllUsers(userName: String): List<UserEntity>

    @Query("DElETE FROM users WHERE name = :name")
    fun delete(name: String)
}