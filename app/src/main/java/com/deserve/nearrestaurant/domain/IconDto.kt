package com.deserve.nearrestaurant.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IconDto(
    @SerializedName("prefix")
    @Expose
    var prefix : String? = null,

    @SerializedName("suffix")
    @Expose
    var suffix : String? = null
)
