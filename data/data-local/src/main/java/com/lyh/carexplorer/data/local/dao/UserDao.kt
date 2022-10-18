package com.lyh.carexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lyh.carexplorer.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query(value = "SELECT * FROM ${UserEntity.TABLE_NAME}")
    suspend fun getUsers(): List<UserEntity>
}