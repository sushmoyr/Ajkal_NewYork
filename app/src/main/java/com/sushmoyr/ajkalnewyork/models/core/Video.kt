package com.sushmoyr.ajkalnewyork.models.core

import com.google.gson.annotations.SerializedName
import com.sushmoyr.ajkalnewyork.utils.Constants.AJKAL_URL

data class Video(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("video_title")
    val videoTitle: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("video_description")
    val videoDescription: String = "",
    @SerializedName("video_link")
    val videoLink: String = "",
    @SerializedName("image")
    val _image: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
){
    val image get() = AJKAL_URL + _image
}
