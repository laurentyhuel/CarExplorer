package com.lyh.carexplorer.domain

import app.cash.turbine.test
import com.lyh.carexplorer.domain.model.CarModel
import com.lyh.carexplorer.domain.repository.ICarRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CarUseCaseTest {

    private val cars = List(10) { index -> createCarModel(index) }

    private val carRepository = mockk<ICarRepository>()
    private val carUseCase = CarUseCase(carRepository)

    @Test
    fun `WHEN get cars THEN return cars`() = runTest {

        coEvery { carRepository.getCars() } returns flow { emit(Result.success(cars)) }
        carUseCase.getCars(null)
            .test {
                val result = awaitItem()

                assertTrue(result.isSuccess)
                val resultSuccess = result.getOrThrow()

                assertEquals(cars.size, resultSuccess.size)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN search cars with empty query THEN return all cars`() = runTest {

        coEvery { carRepository.getCars() } returns flow { emit(Result.success(cars)) }
        carUseCase.getCars("")
            .test {
                val result = awaitItem()

                assertTrue(result.isSuccess)
                val resultSuccess = result.getOrThrow()

                assertEquals(cars.size, resultSuccess.size)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN search cars THEN return expected cars`() = runTest {

        coEvery { carRepository.searchCars("query") } returns flow { emit(cars) }
        carUseCase.getCars("query")
            .test {
                val result = awaitItem()

                assertTrue(result.isSuccess)
                val resultSuccess = result.getOrThrow()

                assertEquals(cars.size, resultSuccess.size)

                awaitComplete()
            }
    }

    @Test
    fun `WHEN get car by id THEN return expected car`() = runTest {

        val car = createCarModel(5)

        coEvery { carRepository.getCarById(car.id) } returns flow { emit(Result.success(car)) }
        carUseCase.getCarById(car.id)
            .test {
                val result = awaitItem()

                assertTrue(result.isSuccess)
                val resultSuccess = result.getOrThrow()

                assertEquals(car.id, resultSuccess.id)
                assertEquals(car.model, resultSuccess.model)

                awaitComplete()
            }
    }

    private fun createCarModel(id: Int) = CarModel(
        1,
        "Make $id",
        "Model $id",
        2010 + id,
        "https://myurl.com/tiny/$id.jpg",
        emptyList()
    )
}
