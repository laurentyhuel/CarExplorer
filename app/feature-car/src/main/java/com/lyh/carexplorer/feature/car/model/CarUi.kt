package com.lyh.carexplorer.feature.car.model

data class CarUi(
    val id: Int,
    val make: String,
    val model: String,
    val year: Int,
    val picture: String,
    val equipments: List<String>
)