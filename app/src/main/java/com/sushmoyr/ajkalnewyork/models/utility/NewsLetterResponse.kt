package com.sushmoyr.ajkalnewyork.models.utility


import com.google.gson.annotations.SerializedName

data class NewsLetterResponse(
    @SerializedName("email")
    val email: List<String> = listOf(),
    @SerializedName("message")
    val message: String = ""
)