package com.sushmoyr.ajkalnewyork.models.utility

import com.google.gson.annotations.SerializedName

data class TransactionInfo(
    val adId: String,
    val user_id: String,
    val payment_method: String,
    val balance_transaction: String,
    val currency: String,
    val amount: String,
    @SerializedName("country_code")
    val countryCode: String = "",
    @SerializedName("country_name")
    val countryName: String = "",
    @SerializedName("city")
    val city: Any? = null,
    @SerializedName("postal")
    val postal: Any? = null,
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("IPv4")
    val iPv4: String = "",
    @SerializedName("state")
    val state: Any? = null
)
