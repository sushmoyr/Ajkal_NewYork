package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("brand")
    val brand: String = "",
    @SerializedName("checks")
    val checks: Checks = Checks(),
    @SerializedName("country")
    val country: String = "",
    @SerializedName("exp_month")
    val expMonth: Int = 0,
    @SerializedName("exp_year")
    val expYear: Int = 0,
    @SerializedName("fingerprint")
    val fingerprint: String = "",
    @SerializedName("funding")
    val funding: String = "",
    @SerializedName("installments")
    val installments: Any? = null,
    @SerializedName("last4")
    val last4: String = "",
    @SerializedName("network")
    val network: String = "",
    @SerializedName("three_d_secure")
    val threeDSecure: String? = null,
    @SerializedName("wallet")
    val wallet: String? = null
)