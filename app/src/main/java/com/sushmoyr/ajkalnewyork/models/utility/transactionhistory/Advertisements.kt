package com.sushmoyr.ajkalnewyork.models.utility.transactionhistory


import com.google.gson.annotations.SerializedName

data class Advertisements(
    @SerializedName("ad_image")
    val adImage: String = "",
    @SerializedName("ad_title")
    val adTitle: String = "",
    @SerializedName("size_name")
    val sizeName: String = "",
    @SerializedName("height")
    val height: String = "",
    @SerializedName("width")
    val width: String = "",
    @SerializedName("perday_amount")
    val perdayAmount: String = "",
    @SerializedName("for_day")
    val forDay: String = "",
    @SerializedName("ad_amount")
    val adAmount: String = "",
    @SerializedName("ad_status")
    val adStatus: Int = 0,
    @SerializedName("payment_type")
    val paymentType: String = "",
    @SerializedName("amount")
    val amount: String = "",
    @SerializedName("balance_transaction")
    val balanceTransaction: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("payment_status")
    val paymentStatus: String = ""
)