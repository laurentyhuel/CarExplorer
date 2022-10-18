package com.lyh.carexplorer.feature.car.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyh.carexplorer.domain.CarUseCase
import com.lyh.carexplorer.domain.core.ResultError
import com.lyh.carexplorer.domain.core.ResultException
import com.lyh.carexplorer.domain.core.ResultSuccess
import com.lyh.carexplorer.feature.car.R
import com.lyh.carexplorer.feature.car.mapper.toUi
import com.lyh.carexplorer.feature.car.model.CarUi
import com.lyh.carexplorer.feature.car.nav.CarDestination
import com.lyh.carexplorer.feature.core.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

class CarViewModel(
    savedStateHandle: SavedStateHandle,
    private val carUseCase: CarUseCase
) : ViewModel() {

    private val carId: Int =
        checkNotNull(savedStateHandle[CarDestination.carIdArg])

    val car: StateFlow<Resource<CarUi>> =
        getCarFlow(carId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ResourceLoading()
            )

    private fun getCarFlow(id: Int): Flow<Resource<CarUi>> =
        carUseCase.getCarById(id)
            .map {
                when (it) {
                    is ResultSuccess -> {
                        val car = it.data.toUi()
                        ResourceSuccess(car)
                    }
                    is ResultError -> {
                        Timber.e("Failed to getCar")
                        ResourceError(
                            errorMessage = ErrorMessage.ErrorMessageString(
                                it.message
                            )
                        )
                    }
                    is ResultException -> {
                        Timber.e(it.throwable, "Error when getCar")
                        ResourceError(
                            errorMessage = ErrorMessage.ErrorMessageResource(
                                R.string.get_car_exception
                            )
                        )
                    }
                }
            }.onStart {
                emit(ResourceLoading())
            }
}
