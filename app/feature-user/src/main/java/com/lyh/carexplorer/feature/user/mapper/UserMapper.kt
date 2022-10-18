package com.lyh.carexplorer.feature.user.mapper

import com.lyh.carexplorer.domain.model.UserModel
import com.lyh.carexplorer.feature.user.model.UserUi

fun UserUi.toModel() = UserModel(
    this.id,
    this.photoUri,
    this.firstName,
    this.lastName,
    this.address,
    this.birthdayString.toEpochTimestamp()
)