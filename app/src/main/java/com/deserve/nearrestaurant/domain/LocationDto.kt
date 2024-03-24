package com.deserve.nearrestaurant.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("address")
    @Expose
    var address: String? = null,

    @SerializedName("address_extended")
    @Expose
    var addressExtended: String? = null,

    @SerializedName("country")
    @Expose
    var country: String? = null,

    @SerializedName("formatted_address")
    @Expose
    var formattedAddress : String? = null,

    @SerializedName("locality")
    @Expose
    var locality: String? = null,

    @SerializedName("postcode")
    @Expose
    var postcode: String? = null,

    @SerializedName("region")
    @Expose
    var region: String? = null
)
