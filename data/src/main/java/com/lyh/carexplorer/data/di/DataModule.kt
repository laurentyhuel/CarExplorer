package com.lyh.carexplorer.data.di

import com.lyh.carexplorer.data.CarRepository
import com.lyh.carexplorer.data.UserRepository
import com.lyh.carexplorer.data.core.AppDispatchers
import com.lyh.carexplorer.data.local.di.dataLocalModule
import com.lyh.carexplorer.data.remote.di.getDataRemoteModule
import com.lyh.carexplorer.domain.repository.ICarRepository
import com.lyh.carexplorer.domain.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * Koin module for data
 */
fun getDataModule(isDebugEnabled: Boolean) = module {
    includes(getDataRemoteModule(isDebugEnabled), dataLocalModule)

    single<ICarRepository> { CarRepository(get(), get(), get()) }
    single<IUserRepository> { UserRepository(get()) }
    single { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
}
