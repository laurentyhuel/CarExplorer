package com.lyh.carexplorer.data

import app.cash.turbine.test
import com.lyh.carexplorer.data.core.AppDispatchers
import com.lyh.carexplorer.data.local.dao.CarDao
import com.lyh.carexplorer.data.local.entity.CarEntity
import com.lyh.carexplorer.data.remote.CarApi
import com.lyh.carexplorer.data.remote.dto.CarDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeoutException

class CarRepositoryTest {
    private val carsFromRemote = List(10) { index -> createCarDto(index) }
    private val carsFromLocal = List(15) { index -> createCarEntity(index) }

    private val carApi = mockk<CarApi>()
    private val carDao = mockk<CarDao>(relaxed = true)
    private val dispatchers = AppDispatchers(UnconfinedTestDispatcher(), UnconfinedTestDispatcher())
    private val carRepository = CarRepository(carApi, carDao, dispatchers)

    @Test
    fun `WHEN get new cars from remote THEN fetch, sync locally and return cars`() = runTest {

        coEvery { carApi.getCars() } returns Result.success(carsFromRemote)
        coEvery { carDao.getCars() } returns carsFromLocal
        carRepository.getCars().test {
            val result = awaitItem()

            assertTrue(result.isSuccess)
            val resultSuccess = result.getOrThrow()

            assertEquals(carsFromLocal.size, resultSuccess.size)

            awaitComplete()
        }
    }

    @Test
    fun `WHEN get car by id THEN return expected car`() = runTest {
        val car = createCarEntity(5)

        coEvery { carDao.getCarById(car.id) } returns car

        carRepository.getCarById(car.id)
            .test {
                val result = awaitItem()

                assertTrue(result.isSuccess)
                val resultSuccess = result.getOrThrow()

                assertEquals(car.id, resultSuccess.id)
                assertEquals(car.model, resultSuccess.model)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get car by id return exception THEN return ResultException`() = runTest {

        coEvery { carDao.getCarById(5) } throws TimeoutException()

        carRepository.getCarById(5).test {
            val result = awaitItem()

            assertTrue(result.isFailure)

            awaitComplete()
        }
    }

    private fun createCarDto(id: Int) = CarDto(
        "make $id",
        "model $id",
        2010 + id,
        "https://myurl.com/$id.jpg",
        listOf("GPS", "Air bag")
    )

    private fun createCarEntity(id: Int) = CarEntity(
        1,
        "make $id",
        "model $id",
        2010 + id,
        "https://myurl.com/$id.jpg",
        "GPS,Air bag"
    )
}
