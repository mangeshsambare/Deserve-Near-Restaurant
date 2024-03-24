package com.deserve.nearrestaurant.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id")
    @Expose
    var id: Int?    = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("short_name")
    @Expose
    var shortName: String? = null,

    @SerializedName("plural_name")
    @Expose
    var pluralName: String? = null,

    @SerializedName("icon")
    @Expose
    var iconDto: IconDto? = IconDto()
)
