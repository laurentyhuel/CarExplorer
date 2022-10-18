package com.lyh.carexplorer.feature.user.di

import com.lyh.carexplorer.feature.user.detail.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureUserModule = module {
    viewModel { UserViewModel(get(), get()) }
}
