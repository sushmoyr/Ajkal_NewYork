package com.sushmoyr.ajkalnewyork.models.core.locations


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("country")
    val country: List<Country> = listOf()
)