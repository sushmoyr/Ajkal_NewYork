package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("amount")
    val amount: Int = 0,
    @SerializedName("canceledAt")
    val canceledAt: Int = 0,
    @SerializedName("clientSecret")
    val clientSecret: String = "",
    @SerializedName("created")
    val created: Int = 0,
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("status")
    val status: String = "",
)