package com.deserve.nearrestaurant.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetPlaceImageUseCase {
    private val placesRepository = PlacesRepository()

    operator fun invoke(apiKey: String, id: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = placesRepository.getRestaurantImage(apiKey, id)
            response?.let {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            if (e is AppException.BadRequestException) {
                emit(Resource.Error(AppErrors.AppException("please check URl")))
            } else if (e is AppException.InvalidAuthException) {
                emit(Resource.Error(AppErrors.InvalidApiKey))
            } else {
                emit(Resource.Error(AppErrors.AppException(e.message ?: "Exception")))
            }
        }
    }
}