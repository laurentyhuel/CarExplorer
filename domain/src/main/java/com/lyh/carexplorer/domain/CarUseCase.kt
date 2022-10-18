package com.lyh.carexplorer.domain

import com.lyh.carexplorer.domain.core.Result
import com.lyh.carexplorer.domain.core.ResultSuccess
import com.lyh.carexplorer.domain.model.CarModel
import com.lyh.carexplorer.domain.repository.ICarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CarUseCase(private val carRepository: ICarRepository) {

    fun getCars(search: String?): Flow<Result<List<CarModel>>> {
        if (search.isNullOrBlank()) {
            return carRepository.getCars()
        }
        return carRepository.searchCars(search).map { ResultSuccess(it) }
    }

    fun getCarById(id: Int) = carRepository.getCarById(id)
}
