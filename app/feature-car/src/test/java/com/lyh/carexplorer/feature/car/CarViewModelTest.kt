package com.lyh.carexplorer.feature.car

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.lyh.carexplorer.domain.CarUseCase
import com.lyh.carexplorer.domain.core.ResultError
import com.lyh.carexplorer.domain.core.ResultSuccess
import com.lyh.carexplorer.domain.model.CarModel
import com.lyh.carexplorer.feature.car.detail.CarViewModel
import com.lyh.carexplorer.feature.car.model.CarUi
import com.lyh.carexplorer.feature.car.nav.CarDestination
import com.lyh.carexplorer.feature.car.util.CoroutinesTestExtension
import com.lyh.carexplorer.feature.car.util.InstantExecutorExtension
import com.lyh.carexplorer.feature.core.ResourceError
import com.lyh.carexplorer.feature.core.ResourceLoading
import com.lyh.carexplorer.feature.core.ResourceSuccess
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class CarViewModelTest {

    private val carId = 5
    private val savedStateHandle = mockk<SavedStateHandle> {
        every { get<Int>(CarDestination.carIdArg) } returns carId
    }

    @Test
    fun `WHEN get car by id succeed THEN get data`() = runTest {
        val carUseCase = mockk<CarUseCase> {
            every { getCarById(carId) } returns flowOf(ResultSuccess(createCarModel(carId)))
        }
        val carViewModel = CarViewModel(savedStateHandle, carUseCase)
        val car = createCarModel(carId)

        carViewModel.car.test {
            val result = awaitItem()
            assertTrue(result is ResourceSuccess)
            val carResult = result as ResourceSuccess
            assertEquals(carId, carResult.data.id)
            assertEquals(car.model, carResult.data.model)
        }
    }

    @Test
    fun `WHEN init THEN get car return loading`() = runTest {
        val carUseCaseRelaxed = mockk<CarUseCase>(relaxed = true)
        val carViewModel = CarViewModel(savedStateHandle, carUseCaseRelaxed)


        carViewModel.car.test {
            val result = awaitItem()
            assertTrue(result is ResourceLoading)
        }
    }

    @Test
    fun `WHEN get car by id failed THEN get data`() = runTest {
        val carUseCase = mockk<CarUseCase> {
            every { getCarById(carId) } returns flowOf(ResultError(400, "Bad request"))
        }
        val carViewModel = CarViewModel(savedStateHandle, carUseCase)

        carViewModel.car.test {
            val result = awaitItem()
            assertTrue(result is ResourceError)
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
}
