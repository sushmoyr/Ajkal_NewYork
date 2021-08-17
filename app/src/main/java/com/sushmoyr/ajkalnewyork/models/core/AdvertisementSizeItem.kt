package com.sushmoyr.ajkalnewyork.models.core


import com.google.gson.annotations.SerializedName

data class AdvertisementSizeItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("height")
    val height: String = "",
    @SerializedName("width")
    val width: String = "",
    @SerializedName("amount")
    val amount: String = "",
    @SerializedName("capacity")
    val capacity: String = "",
    @SerializedName("submited")
    val submitted: String = "",
    @SerializedName("available")
    val available: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)