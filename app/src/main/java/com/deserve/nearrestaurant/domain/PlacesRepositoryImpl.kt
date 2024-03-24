package com.deserve.nearrestaurant.domain

interface PlacesRepositoryImpl {

    suspend fun getRestaurants(
        latitude: String,
        longitude: String,
        apiKey: String
    ): List<RestaurantDto>
}