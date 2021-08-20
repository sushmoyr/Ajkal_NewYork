package com.sushmoyr.ajkalnewyork.models.core.ads


import com.google.gson.annotations.SerializedName

data class Advertisement(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("exp_date")
    val expDate: String = "",
    @SerializedName("position")
    val position: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""

)