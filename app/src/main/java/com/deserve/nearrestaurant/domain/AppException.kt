package com.deserve.nearrestaurant.domain

sealed class AppException: Exception() {
    object BadRequestException: AppException()
    object InvalidAuthException: AppException()
}