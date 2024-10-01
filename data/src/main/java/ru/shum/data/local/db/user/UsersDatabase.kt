package ru.shum.data.local.db.user

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.shum.data.local.db.user.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UsersDatabase: RoomDatabase() {

    abstract fun usersDao(): UserDao
}