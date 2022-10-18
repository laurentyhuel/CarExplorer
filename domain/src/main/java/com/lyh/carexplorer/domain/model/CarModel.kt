package com.lyh.carexplorer.domain.model

data class CarModel(
    val id: Int,
    val make: String,
    val model: String,
    val year: Int,
    val picture: String,
    val equipments: List<String>
)