package com.sushmoyr.ajkalnewyork.models.stripe

data class PaymentIntentModel(
    val amount: Int,
    val currency: String = "usd",
    val clientSecret: String? = null
)