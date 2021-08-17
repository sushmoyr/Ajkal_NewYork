package com.sushmoyr.ajkalnewyork.models.core


import com.google.gson.annotations.SerializedName

data class Division(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("country_id")
    val countryId: String = "",
    @SerializedName("division_name")
    val divisionName: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)