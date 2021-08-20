package com.sushmoyr.ajkalnewyork.models.core.ads


import com.google.gson.annotations.SerializedName

data class AdPayment(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("advertisement_id")
    val advertisementId: String = "",
    @SerializedName("payment_type")
    val paymentType: String = "",
    @SerializedName("payment_method")
    val paymentMethod: String = "",
    @SerializedName("balance_transaction")
    val balanceTransaction: String = "",
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("amount")
    val amount: String = "",
    @SerializedName("country_code")
    val countryCode: String = "",
    @SerializedName("country_name")
    val countryName: String = "",
    @SerializedName("city")
    val city: String = "",
    @SerializedName("postal")
    val postal: Any? = null,
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("ipv4")
    val ipv4: String = "",
    @SerializedName("state")
    val state: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)