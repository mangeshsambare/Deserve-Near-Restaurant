package com.deserve.nearrestaurant.restaurant_detail

import com.deserve.nearrestaurant.domain.Restaurant

data class RestaurantScreenState(
    val isLoading: Boolean = false,
    val restaurantList: List<Restaurant> = listOf(),
    val error: String? = null
)