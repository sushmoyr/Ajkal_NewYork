package com.sushmoyr.ajkalnewyork.models.core.locations


import com.google.gson.annotations.SerializedName

data class Division(
    @SerializedName("country_id")
    val countryId: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("division_name")
    val divisionName: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)