package com.lyh.carexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lyh.carexplorer.data.local.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCars(cars: List<CarEntity>)

    @Query("DELETE FROM ${CarEntity.TABLE_NAME}")
    suspend fun deleteAll()

    @Query(value = "SELECT * FROM ${CarEntity.TABLE_NAME}")
    fun getCars(): List<CarEntity>

    @Query(value = "SELECT * FROM ${CarEntity.TABLE_NAME} WHERE LOWER(make) LIKE :query OR LOWER(model) LIKE :query")
    fun searchCars(query: String): Flow<List<CarEntity>>

    @Query(value = "SELECT * FROM ${CarEntity.TABLE_NAME} WHERE id = :id")
    suspend fun getCarById(id: Int): CarEntity
}