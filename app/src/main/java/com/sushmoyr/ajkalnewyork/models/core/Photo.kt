package com.sushmoyr.ajkalnewyork.models.core

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("photo_title")
    val photoTitle: String = "",
    @SerializedName("photo_description")
    val photoDescription: String = "",
    @SerializedName("image")
    val imagePath: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)
