package com.deserve.nearrestaurant.domain

sealed class Resource<T>(val data: T? = null, val error: AppErrors? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<Nothing>(error: AppErrors) : Resource<Nothing>(error = error)
    class Loading<Nothing> : Resource<Nothing>()
}