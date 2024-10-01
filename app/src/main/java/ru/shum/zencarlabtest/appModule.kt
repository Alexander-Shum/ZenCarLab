package ru.shum.zencarlabtest

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.shum.zencarlabtest.presentation.auth.AuthViewModel
import ru.shum.zencarlabtest.presentation.home.HomeViewModel

val appModule = module {
    factoryOf(::AuthViewModel)
    factoryOf(::HomeViewModel)
}