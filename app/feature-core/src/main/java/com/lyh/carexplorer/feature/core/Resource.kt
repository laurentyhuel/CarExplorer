package com.lyh.carexplorer.feature.core

sealed interface Resource<T>

class ResourceSuccess<T>(val data: T) : Resource<T>
class ResourceLoading<T> : Resource<T>
class ResourceError<T>(val errorMessage: String?) : Resource<T>





