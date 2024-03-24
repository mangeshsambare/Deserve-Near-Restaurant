package com.deserve.nearrestaurant.domain

class PlacesRepository: PlacesRepositoryImpl {

    companion object {
        private const val RADIUS = "3000"
        private const val RESTAURANTS_ID = "4d4b7105d754a06374d81259"
        private const val FOUR_SQUARE_URL = "https://api.foursquare.com/v3/places/search?&radius=" +
                "${RADIUS}&categories=${RESTAURANTS_ID}&sort=DISTANCE&limit=15"
    }

    override suspend fun getRestaurants(
        latitude: String,
        longitude: String,
        apiKey: String
    ): List<RestaurantDto> {
        val request = NetworkRequest.createRequest(FOUR_SQUARE_URL)
        request.setRequestProperty("Authorization", apiKey)
        val resultDto: ResultDto? = NetworkRequest.makeRequest<ResultDto>(request)
        return  resultDto?.restaurantDtoList ?: emptyList()
    }
}