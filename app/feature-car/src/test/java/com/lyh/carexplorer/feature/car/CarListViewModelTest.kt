package com.lyh.carexplorer.feature.car

import app.cash.turbine.test
import com.lyh.carexplorer.domain.CarUseCase
import com.lyh.carexplorer.domain.core.ResultSuccess
import com.lyh.carexplorer.domain.model.CarModel
import com.lyh.carexplorer.feature.car.list.CarListViewModel
import com.lyh.carexplorer.feature.car.model.CarUi
import com.lyh.carexplorer.feature.car.util.CoroutinesTestExtension
import com.lyh.carexplorer.feature.car.util.InstantExecutorExtension
import com.lyh.carexplorer.feature.core.ResourceSuccess
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(
    InstantExecutorExtension::class,
    CoroutinesTestExtension::class
)
class CarListViewModelTest {

    private val cars = List(10) { index -> createCarModel(index) }
    private val filteredCars = List(5) { index -> createCarModel(index) }

    private val carUseCase = mockk<CarUseCase>()

    private lateinit var carListViewModel: CarListViewModel


    @Test
    fun `WHEN get cars THEN return state with cars`() = runTest {
        every { carUseCase.getCars("") } returns flowOf(ResultSuccess(cars))
        carListViewModel = CarListViewModel(carUseCase)


        carListViewModel.cars.test {

            //TODO analyze why initialized twice
            val otherSuccess = awaitItem()


            val dataSuccess = awaitItem()
            assertTrue(dataSuccess is ResourceSuccess)
            val resultSuccess = dataSuccess as ResourceSuccess
            assertEquals(cars.size, resultSuccess.data.size)

        }
    }

    @Test
    fun `WHEN search cars THEN return state with cars`() = runTest {
        every { carUseCase.getCars("") } returns flowOf(ResultSuccess(cars))
        every { carUseCase.getCars("query") } returns flowOf(ResultSuccess(filteredCars))
        carListViewModel = CarListViewModel(carUseCase)


        carListViewModel.cars.test {
            //TODO analyze why initialized twice
            val otherSuccess = awaitItem()

            val dataSuccess = awaitItem()
            assertTrue(dataSuccess is ResourceSuccess)
            val resultSuccess = dataSuccess as ResourceSuccess
            assertEquals(cars.size, resultSuccess.data.size)

            carListViewModel.setSearchText("query")

            val searchSuccess = awaitItem()
            assertTrue(searchSuccess is ResourceSuccess)
            val searchResultSuccess = searchSuccess as ResourceSuccess
            assertEquals(filteredCars.size, searchResultSuccess.data.size)
        }
    }

    private fun createCarModel(id: Int) = CarModel(
        id,
        "Make $id",
        "Model $id",
        2010 + id,
        "https://myurl.com/tiny/$id.jpg",
        emptyList()
    )

    private fun createCarUi(id: Int) = CarUi(
        id,
        "Make $id",
        "Model $id",
        2010 + id,
        "https://myurl.com/tiny/$id.jpg",
        emptyList()
    )
}
