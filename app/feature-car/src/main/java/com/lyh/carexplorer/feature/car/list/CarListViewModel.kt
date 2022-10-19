package com.lyh.carexplorer.feature.car.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyh.carexplorer.domain.CarUseCase
import com.lyh.carexplorer.feature.car.mapper.toUis
import com.lyh.carexplorer.feature.car.model.CarUi
import com.lyh.carexplorer.feature.core.Resource
import com.lyh.carexplorer.feature.core.ResourceError
import com.lyh.carexplorer.feature.core.ResourceLoading
import com.lyh.carexplorer.feature.core.ResourceSuccess
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class CarListViewModel(
    private val carUseCase: CarUseCase
) : ViewModel() {

    private val _textSearch = MutableStateFlow("")
    val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    init {
        viewModelScope.launch {
            // As soon the textSearch flow changes,
            // if the user stops typing for 500ms, the item will be emitted
            textSearch.debounce(DEBOUNCE_TIME).collect {
                triggerCars()
            }
        }
    }

    fun setSearchText(it: String) {
        _textSearch.value = it
    }

    private val carsTrigger: MutableSharedFlow<Unit> = MutableSharedFlow(replay = 1)

    init {
        triggerCars()
    }

    val cars: StateFlow<Resource<List<CarUi>>> =
        carsTrigger.flatMapLatest {
            getCarsFlow()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResourceLoading()
        )

    private fun getCarsFlow(): Flow<Resource<List<CarUi>>> = carUseCase.getCars(_textSearch.value)
        .map { result ->

            result.fold(
                onSuccess = { ResourceSuccess(it.toUis()) },
                onFailure = {
                    Timber.e(it, "Failed to get cars, search=${_textSearch.value}")
                    ResourceError(it.message)
                }
            )
        }
        .onStart {
            emit(ResourceLoading())
        }

    fun triggerCars() = viewModelScope.launch {
        carsTrigger.emit(Unit)
    }

    companion object {
        const val DEBOUNCE_TIME = 500L
    }
}
