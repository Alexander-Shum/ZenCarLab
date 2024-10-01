package ru.shum.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.shum.domain.usecase.SaveUserUseCase
import ru.shum.domain.usecase.LoginUserUseCase
import ru.shum.domain.usecase.UserExistsUseCase
import ru.shum.domain.usecase.LogoutUseCase
import ru.shum.domain.usecase.LoggedInUseCase
import ru.shum.domain.usecase.GetUserByNameUseCase
import ru.shum.domain.usecase.GetLoggedUserNameUseCase
import ru.shum.domain.usecase.GetAllUsersUseCase
import ru.shum.domain.usecase.DeleteUserUseCase

val domainModule = module {
    singleOf(::SaveUserUseCase)
    singleOf(::LoginUserUseCase)
    singleOf(::UserExistsUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::LoggedInUseCase)
    singleOf(::GetUserByNameUseCase)
    singleOf(::GetLoggedUserNameUseCase)
    singleOf(::GetAllUsersUseCase)
    singleOf(::DeleteUserUseCase)
}