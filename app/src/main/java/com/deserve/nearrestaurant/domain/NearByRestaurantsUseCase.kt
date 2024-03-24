package com.deserve.nearrestaurant.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NearByRestaurantsUseCase {
    private val placesRepository = PlacesRepository()

    operator fun invoke(
        latitude: String,
        longitude: String,
        apiKey: String
    ): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.Loading())
        try {
            val response = placesRepository.getRestaurants(latitude, longitude, apiKey)
            val restaurantList = mutableListOf<Restaurant>()
            if (response.isNotEmpty()) {
                response.forEach {
                    val restaurant = it.toRestaurant()
                    restaurantList.add(restaurant)
                }
            }
            emit(Resource.Success(restaurantList))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Error"))
        }
    }
}