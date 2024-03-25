package com.deserve.nearrestaurant.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class IconDto(
    @SerializedName("prefix")
    @Expose
    var prefix : String? = null,

    @SerializedName("suffix")
    @Expose
    var suffix : String? = null,

    @SerializedName("width")
    @Expose
    val width: Int? = 0,

    @SerializedName("height")
    @Expose
    val height: Int? = 0
)
