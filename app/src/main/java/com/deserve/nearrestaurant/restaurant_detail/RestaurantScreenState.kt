package com.deserve.nearrestaurant.restaurant_detail

import android.location.Location
import com.deserve.nearrestaurant.domain.AppErrors
import com.deserve.nearrestaurant.domain.Restaurant

data class RestaurantScreenState(
    val isLoading: Boolean = false,
    val location: Location? = null,
    val restaurantList: List<Restaurant> = listOf(),
    val error: AppErrors? = null
)