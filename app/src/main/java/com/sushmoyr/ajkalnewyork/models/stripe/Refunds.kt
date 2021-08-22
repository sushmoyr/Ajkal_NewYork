package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class Refunds(
    @SerializedName("object")
    val objectX: String = "",
    @SerializedName("data")
    val `data`: List<Any> = listOf(),
    @SerializedName("has_more")
    val hasMore: Boolean = false,
    @SerializedName("url")
    val url: String = ""
)