package com.lyh.carexplorer.data.mapper

import com.lyh.carexplorer.data.local.entity.CarEntity
import com.lyh.carexplorer.data.remote.dto.CarDto
import com.lyh.carexplorer.domain.model.CarModel

internal fun List<CarEntity>.toModels() = this.map { it.toModel() }

internal fun CarEntity.toModel() = CarModel(
    this.id,
    this.make,
    this.model,
    this.year,
    this.pictureUrl,
    if (this.equipments.isBlank()) emptyList() else this.equipments.split(EQUIPMENTS_DELIMITER)
)

internal fun List<CarDto>.toEntities() = this.map { it.toEntity() }

internal fun CarDto.toEntity() = CarEntity(
    make = this.make,
    model = this.model,
    year = this.year,
    pictureUrl = this.picture,
    equipments = this.equipments?.joinToString(separator = EQUIPMENTS_DELIMITER) ?: ""
)

private const val EQUIPMENTS_DELIMITER = ","

