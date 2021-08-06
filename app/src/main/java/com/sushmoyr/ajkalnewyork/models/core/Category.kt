package com.sushmoyr.ajkalnewyork.models.core

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("category_name")
    val categoryName: String = "",
    @SerializedName("category_logo")
    val slug: String = "",
)