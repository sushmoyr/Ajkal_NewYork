package com.sushmoyr.ajkalnewyork.models.utility


import com.google.gson.annotations.SerializedName

data class Success(
    @SerializedName("user")
    val user: User = User()
)