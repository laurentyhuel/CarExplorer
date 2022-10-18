package com.lyh.carexplorer.domain.repository

import com.lyh.carexplorer.domain.core.Result
import com.lyh.carexplorer.domain.model.CarModel
import kotlinx.coroutines.flow.Flow

interface ICarRepository {

    fun getCars(): Flow<Result<List<CarModel>>>

    fun searchCars(query: String): Flow<List<CarModel>>

    fun getCarById(id: Int): Flow<Result<CarModel>>
}
