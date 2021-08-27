package com.sushmoyr.ajkalnewyork.models.core.locations


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("country_name")
    val countryName: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("districts")
    val districts: List<District> = listOf(),
    @SerializedName("divisions")
    val divisions: List<Division> = listOf(),
    @SerializedName("id")
    val id: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)