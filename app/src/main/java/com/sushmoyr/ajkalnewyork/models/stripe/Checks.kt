package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class Checks(
    @SerializedName("address_line1_check")
    val addressLine1Check: String? = null,
    @SerializedName("address_postal_code_check")
    val addressPostalCodeCheck: String? = null,
    @SerializedName("cvc_check")
    val cvcCheck: String = ""
)