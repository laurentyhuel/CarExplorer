package com.lyh.carexplorer.data.local.di

import android.content.Context
import androidx.room.Room
import com.lyh.carexplorer.data.local.AppDatabase
import org.koin.dsl.module

/**
 * Koin module for data-local
 */
val dataLocalModule = module {
    single { providesAppDatabase(get()) }
    single { providesCarDao(get()) }
    single { providesUserDao(get()) }
}

private fun providesUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

private fun providesCarDao(appDatabase: AppDatabase) = appDatabase.carDao()

private fun providesAppDatabase(
    context: Context,
): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    DATABASE_NAME
).build()

private const val DATABASE_NAME = "car-explorer-database"