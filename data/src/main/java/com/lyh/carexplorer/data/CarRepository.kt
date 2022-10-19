package com.lyh.carexplorer.data

import com.lyh.carexplorer.data.core.AppDispatchers
import com.lyh.carexplorer.data.core.fetchAndStoreLocally
import com.lyh.carexplorer.data.local.dao.CarDao
import com.lyh.carexplorer.data.mapper.toEntities
import com.lyh.carexplorer.data.mapper.toModel
import com.lyh.carexplorer.data.mapper.toModels
import com.lyh.carexplorer.data.remote.CarApi
import com.lyh.carexplorer.data.remote.dto.CarDto
import com.lyh.carexplorer.domain.model.CarModel
import com.lyh.carexplorer.domain.repository.ICarRepository
import kotlinx.coroutines.flow.*
import java.util.*

class CarRepository(
    private val carApi: CarApi,
    private val carDao: CarDao,
    private val dispatchers: AppDispatchers
) : ICarRepository {

    private var lastFetchCars: Date? = null

    override fun getCarById(id: Int): Flow<Result<CarModel>> =
        flow {
            emit(Result.success(carDao.getCarById(id).toModel()))
        }.catch {
            emit(Result.failure(it))
        }.flowOn(dispatchers.io)

    override fun getCars(): Flow<Result<List<CarModel>>> = fetchAndStoreLocally(
        ::getRemoteCars,
        ::syncCars,
        ::getLocalCars,
        lastFetchCars,
        ::fetchRemoteSuccessCallback
    ).catch {
        emit(Result.failure(it))
    }.flowOn(dispatchers.io)

    override fun searchCars(query: String): Flow<List<CarModel>> =
        carDao.searchCars("%${query.lowercase()}%").map { it.toModels() }

    private fun fetchRemoteSuccessCallback() {
        lastFetchCars = Date()
    }

    private suspend fun getRemoteCars() = carApi.getCars()

    private fun getLocalCars() = carDao.getCars().toModels()

    private suspend fun syncCars(carsFromRemote: List<CarDto>): List<CarModel> {
        //TODO to improve: no id from WS, so delete all local data each time
        carDao.deleteAll()

        carDao.insertCars(carsFromRemote.toEntities())

        return getLocalCars()
    }
}
