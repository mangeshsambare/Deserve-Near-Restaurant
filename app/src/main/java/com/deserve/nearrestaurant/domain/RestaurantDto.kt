package com.deserve.nearrestaurant.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RestaurantDto(
    @SerializedName("fsq_id")
    @Expose
    var fsqId: String? = null,

    @SerializedName("categories")
    @Expose
    var categories: ArrayList<CategoryDto> = arrayListOf(),

    @SerializedName("chains")
    @Expose
    var chains: ArrayList<String> = arrayListOf(),

    @SerializedName("closed_bucket")
    @Expose
    var closedBucket: String? = null,

    @SerializedName("distance")
    @Expose
    var distance: Int? = null,

    @SerializedName("link")
    @Expose
    var link: String? = null,

    @SerializedName("location")
    @Expose
    var location: LocationDto? = LocationDto(),

    @SerializedName("name")
    @Expose
    var name: String? = null,
)

fun RestaurantDto.toRestaurant(): Restaurant {
    val address = location?.formattedAddress
    return Restaurant(
        id = fsqId,
        name = name,
        image = null, // set null
        address = address,
        status = closedBucket
    )
}

