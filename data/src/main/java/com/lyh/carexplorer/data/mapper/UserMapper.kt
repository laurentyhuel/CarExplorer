package com.lyh.carexplorer.data.mapper

import com.lyh.carexplorer.data.local.entity.UserEntity
import com.lyh.carexplorer.domain.model.UserModel

internal fun UserModel.toEntity() = UserEntity(
    this.id,
    this.photoUri,
    this.firstName,
    this.lastName,
    this.address,
    this.birthday
)

internal fun UserEntity.toModel() = UserModel(
    this.id,
    this.photoUri,
    this.firstName,
    this.lastName,
    this.address,
    this.birthday
)