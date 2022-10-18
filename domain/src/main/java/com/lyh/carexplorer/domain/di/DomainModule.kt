package com.lyh.carexplorer.domain.di

import com.lyh.carexplorer.domain.CarUseCase
import com.lyh.carexplorer.domain.UserUseCase
import org.koin.dsl.module

/**
 * Koin module for data-remote
 */
val domainModule = module {
    single { CarUseCase(get()) }
    single { UserUseCase(get()) }
}