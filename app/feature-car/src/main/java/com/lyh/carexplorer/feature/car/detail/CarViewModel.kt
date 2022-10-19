package com.lyh.carexplorer.feature.car.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyh.carexplorer.domain.CarUseCase
import com.lyh.carexplorer.feature.car.mapper.toUi
import com.lyh.carexplorer.feature.car.model.CarUi
import com.lyh.carexplorer.feature.car.nav.CarDestination
import com.lyh.carexplorer.feature.core.Resource
import com.lyh.carexplorer.feature.core.ResourceError
import com.lyh.carexplorer.feature.core.ResourceLoading
import com.lyh.carexplorer.feature.core.ResourceSuccess
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
        carUseCase.getCarById(id).map { result ->
            result.fold(
                onSuccess = { ResourceSuccess(it.toUi()) },
                onFailure = {
                    Timber.e(it, "Failed to getCar")
                    ResourceError(it.message)
                }
            )
        }.onStart {
            emit(ResourceLoading())
        }
}
