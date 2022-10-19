package com.lyh.carexplorer.data.remote

import com.lyh.carexplorer.data.remote.dto.CarDto
import retrofit2.Response
import retrofit2.http.GET

interface CarApi {

    @GET("/ncltg/6a74a0143a8202a5597ef3451bde0d5a/raw/8fa93591ad4c3415c9e666f888e549fb8f945eb7/tc-test-ios.json")
    suspend fun getCars(): Result<List<CarDto>>
}

