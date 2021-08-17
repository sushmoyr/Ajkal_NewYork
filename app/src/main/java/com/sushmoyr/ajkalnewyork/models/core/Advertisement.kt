package com.sushmoyr.ajkalnewyork.models.core


import com.google.gson.annotations.SerializedName

data class Advertisement(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("user_id")
    val userId: String? = null,
    @SerializedName("payment_id")
    val paymentId: String? = null,
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
    val updatedAt: String = "",
    @SerializedName("size_id")
    val sizeId: String = "",
    @SerializedName("created_date")
    val createdDate: String = "",
    @SerializedName("for_day")
    val forDay: String = "",
    @SerializedName("amount")
    val amount: String = "",

){
    val adImage get() = "https://ajkal.fastrider.co$_adImage"
}