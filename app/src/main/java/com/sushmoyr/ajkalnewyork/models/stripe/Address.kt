package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("line1")
    val line1: String? = null,
    @SerializedName("line2")
    val line2: String? = null,
    @SerializedName("postal_code")
    val postalCode: String? = null,
    @SerializedName("state")
    val state: String? = null
)