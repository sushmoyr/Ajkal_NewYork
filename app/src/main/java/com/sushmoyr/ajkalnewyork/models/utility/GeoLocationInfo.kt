package com.sushmoyr.ajkalnewyork.models.utility


import com.google.gson.annotations.SerializedName

data class GeoLocationInfo(
    @SerializedName("country_code")
    val countryCode: String = "",
    @SerializedName("country_name")
    val countryName: String = "",
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("postal")
    val postal: String? = null,
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("IPv4")
    val iPv4: String = "",
    @SerializedName("state")
    val state: String? = null
)