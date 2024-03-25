package com.deserve.nearrestaurant.domain

class PlacesRepository: PlacesRepositoryImpl {

    companion object {
        private const val RADIUS = "3000"
        private const val RESTAURANTS_ID = "4d4b7105d754a06374d81259"
        private const val FOUR_SQUARE_PLACES_URL = "https://api.foursquare.com/v3/places/"
        private const val FOUR_SQUARE_SEARCH_URL = FOUR_SQUARE_PLACES_URL+"search?&radius=" +
                "${RADIUS}&categories=${RESTAURANTS_ID}&sort=DISTANCE&limit=15"
    }

    override suspend fun getRestaurants(
        latitude: String,
        longitude: String,
        apiKey: String
    ): List<RestaurantDto> {
        val request = NetworkRequest.createRequest(FOUR_SQUARE_SEARCH_URL)
        request.setRequestProperty("Authorization", apiKey)
        val resultDto: ResultDto? = NetworkRequest.makeRequest<ResultDto>(request)
        return  resultDto?.restaurantDtoList ?: emptyList()
    }

    override suspend fun getRestaurantImage(apiKey: String,
                                            id: String): String? {
        var iconUrl: String? = null
        val photosUrl =
            "$FOUR_SQUARE_PLACES_URL$id/photos?limit=1&sort=POPULAR&classifications=indoor"

        val request = NetworkRequest.createRequest(photosUrl)
        request.setRequestProperty("Authorization", apiKey)
        val iconsList: List<IconDto> = NetworkRequest.makeRequest<List<IconDto>>(request)
        if (iconsList.isNotEmpty()) {
            val iconDto = iconsList.first()
            iconUrl = iconDto.prefix+""+iconDto.width.toString()+"x"+iconDto.height.toString()+
                    ""+iconDto.suffix
        }
        return iconUrl
    }
}