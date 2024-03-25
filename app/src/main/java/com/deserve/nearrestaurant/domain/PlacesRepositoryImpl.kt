package com.deserve.nearrestaurant.domain

interface PlacesRepositoryImpl {

    suspend fun getRestaurants(
        latitude: String,
        longitude: String,
        apiKey: String
    ): List<RestaurantDto>

    suspend fun getRestaurantImage(
        apiKey: String,
        id: String
    ): String?
}