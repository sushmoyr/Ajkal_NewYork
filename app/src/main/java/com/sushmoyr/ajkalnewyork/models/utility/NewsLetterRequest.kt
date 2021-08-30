package com.sushmoyr.ajkalnewyork.models.utility

import com.google.gson.annotations.SerializedName

data class NewsLetterRequest(
    @SerializedName("email")
    val email: String
)
