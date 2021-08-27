package com.sushmoyr.ajkalnewyork.models.core.locations


import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("country_id")
    val countryId: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("district_name")
    val districtName: String = "",
    @SerializedName("division_id")
    val divisionId: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)