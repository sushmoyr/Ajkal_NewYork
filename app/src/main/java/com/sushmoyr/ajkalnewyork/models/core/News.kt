package com.sushmoyr.ajkalnewyork.models.core

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("subcategory_id")
    val subcategoryId: String? = null,
    @SerializedName("country_id")
    val countryId: String? = null,
    @SerializedName("division_id")
    val divisionId: String? = null,
    @SerializedName("district_id")
    val districtId: String? = null,
    @SerializedName("news_title")
    val newsTitle: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("default_image")
    val _defaultImage: String = "",
    @SerializedName("video_id")
    val videoId: String? = null,
    @SerializedName("popular_news")
    val popularNews: String? = null,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("created_by")
    val createdBy: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
): Parcelable{
    val defaultImage get() = "https://ajkal.fastrider.co$_defaultImage"
}

