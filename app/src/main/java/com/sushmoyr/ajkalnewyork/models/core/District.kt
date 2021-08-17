package com.sushmoyr.ajkalnewyork.models.core


import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("country_id")
    val countryId: String = "",
    @SerializedName("division_id")
    val divisionId: String = "",
    @SerializedName("district_name")
    val districtName: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)