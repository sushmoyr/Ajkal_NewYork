package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class PaymentMethodDetails(
    @SerializedName("card")
    val card: Card = Card(),
    @SerializedName("type")
    val type: String = ""
)