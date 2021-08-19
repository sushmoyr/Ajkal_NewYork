package com.sushmoyr.ajkalnewyork.models.utility


import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("email")
    val email: List<String> = listOf(),
    @SerializedName("token")
    val token: String? = null
)