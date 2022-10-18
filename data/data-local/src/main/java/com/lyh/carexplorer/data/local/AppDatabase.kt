package com.lyh.carexplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lyh.carexplorer.data.local.dao.CarDao
import com.lyh.carexplorer.data.local.dao.UserDao
import com.lyh.carexplorer.data.local.entity.CarEntity
import com.lyh.carexplorer.data.local.entity.UserEntity

@Database(entities = [CarEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun userDao(): UserDao
}