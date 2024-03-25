package com.deserve.nearrestaurant.domain

sealed class AppErrors {
    object InvalidApiKey: AppErrors()
    object NoLocationFound: AppErrors()

    data class AppException(val error: String): AppErrors()
    object UnknownError: AppErrors()
}