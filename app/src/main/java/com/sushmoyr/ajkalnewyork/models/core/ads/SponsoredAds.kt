package com.sushmoyr.ajkalnewyork.models.core.ads


import com.google.gson.annotations.SerializedName

data class SponsoredAds(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("is_payment")
    val isPayment: String = "",
    @SerializedName("payment_id")
    val paymentId: String = "",
    @SerializedName("ad_title")
    val adTitle: String = "",
    @SerializedName("ad_link")
    val adLink: String = "",
    @SerializedName("size_id")
    val sizeId: String = "",
    @SerializedName("ad_image")
    val adImage: String = "",
    @SerializedName("created_date")
    val createdDate: String = "",
    @SerializedName("exp_date")
    val expDate: String = "",
    @SerializedName("for_day")
    val forDay: String = "",
    @SerializedName("amount")
    val amount: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)