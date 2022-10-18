package com.lyh.carexplorer.feature.car.di

import com.lyh.carexplorer.feature.car.detail.CarViewModel
import com.lyh.carexplorer.feature.car.list.CarListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureCarModule = module {
    viewModel { CarViewModel(get(), get()) }
    viewModel { CarListViewModel(get()) }
}