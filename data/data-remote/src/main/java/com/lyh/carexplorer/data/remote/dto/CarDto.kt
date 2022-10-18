package com.lyh.carexplorer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarDto(
    val make: String,
    val model: String,
    val year: Int,
    val picture: String,
    val equipments: List<String>?
)
