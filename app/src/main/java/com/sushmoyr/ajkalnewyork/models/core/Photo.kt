package com.sushmoyr.ajkalnewyork.models.core

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("photo_title")
    val photoTitle: String = "",
    @SerializedName("photo_description")
    val photoDescription: String = "",
    @SerializedName("image")
    val _imagePath: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
): Parcelable {
    val imagePath get() ="https://ajkal.fastrider.co$_imagePath"
}
