package com.deserve.nearrestaurant.domain

import com.google.gson.annotations.SerializedName

data class ResultDto(
    @SerializedName("results")
    var restaurantDtoList : ArrayList<RestaurantDto> = arrayListOf(),
)
