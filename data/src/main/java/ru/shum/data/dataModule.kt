package ru.shum.data

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.shum.data.local.db.user.UsersDatabase
import ru.shum.data.local.db.user.UsersDatabase.Companion.MIGRATION_1_2
import ru.shum.data.local.sharedpref.AuthManager
import ru.shum.data.repository.AuthRepositoryImpl
import ru.shum.data.repository.UserRepositoryImpl
import ru.shum.domain.repository.AuthRepository
import ru.shum.domain.repository.UserRepository

val dataModule = module {
    single {
        AuthManager(get())
    }

    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }

    single {
        Room.databaseBuilder(androidContext(), UsersDatabase::class.java, "users_database")
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    single { get<UsersDatabase>().usersDao() }
}