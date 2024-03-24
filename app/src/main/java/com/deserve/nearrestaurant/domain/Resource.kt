package com.deserve.nearrestaurant.domain

sealed class Resource<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<Nothing>(error: String) : Resource<Nothing>(error = error)
    class Loading<Nothing> : Resource<Nothing>()
}