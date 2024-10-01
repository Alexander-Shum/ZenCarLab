package ru.shum.data.local.db.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.shum.data.local.db.user.model.UserEntity

@Database(entities = [UserEntity::class], version = 2)
abstract class UsersDatabase: RoomDatabase() {

    abstract fun usersDao(): UserDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN registrationDate INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE users ADD COLUMN avatarUrl TEXT")
            }
        }
    }
}