package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class BillingDetails(
    @SerializedName("address")
    val address: Address = Address(),
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("phone")
    val phone: String? = null
)