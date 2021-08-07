package com.sushmoyr.ajkalnewyork.models.core


import com.google.gson.annotations.SerializedName

data class Advertisement(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("user_id")
    val userId: String? = null,
    @SerializedName("payment_id")
    val paymentId: Any? = null,
    @SerializedName("ad_title")
    val adTitle: String = "",
    @SerializedName("ad_link")
    val adLink: String = "",
    @SerializedName("ad_image")
    val _adImage: String = "",
    @SerializedName("exp_date")
    val expDate: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
){
    val adImage get() = "https://ajkal.fastrider.co$_adImage"
}