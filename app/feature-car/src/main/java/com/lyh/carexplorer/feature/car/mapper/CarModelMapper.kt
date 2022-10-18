package com.lyh.carexplorer.feature.car.mapper

import com.lyh.carexplorer.domain.model.CarModel
import com.lyh.carexplorer.feature.car.model.CarUi

fun CarModel.toUi() = CarUi(
    this.id,
    this.make,
    this.model,
    this.year,
    this.picture,
    this.equipments
)

fun List<CarModel>.toUis() = this.map { it.toUi() }
