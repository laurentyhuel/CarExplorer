package com.lyh.carexplorer.data.remote.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lyh.carexplorer.data.remote.CarApi
import com.lyh.carexplorer.data.remote.core.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Koin module for data-remote
 */
fun getDataRemoteModule(isDebugEnabled: Boolean) = module {

    // Retrofit and OkHttp setup
    single { Json { ignoreUnknownKeys = true } }
    single { providesOkHttpClient(isDebugEnabled, get()) }
    single { providesRetrofit(get(), get()) }

    // API
    single { providesCarApi(get()) }
}

private fun providesCarApi(retrofit: Retrofit): CarApi =
    retrofit.create(CarApi::class.java)

private fun providesRetrofit(jsonSerializer: Json, okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(jsonSerializer.asConverterFactory(mediaTypeJson.toMediaType()))
    .addCallAdapterFactory(ResultCallAdapterFactory())
    .build()

private fun providesOkHttpClient(isDebugEnabled: Boolean, context: Context): OkHttpClient = OkHttpClient()
    .newBuilder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (isDebugEnabled)
            Level.BODY
        else
            Level.NONE
    })
    .build()

private const val baseUrl = "https://gist.githubusercontent.com/"
private const val mediaTypeJson = "application/json"




