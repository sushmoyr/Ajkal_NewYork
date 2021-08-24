package com.sushmoyr.ajkalnewyork.models.utility

import com.google.gson.annotations.SerializedName
import com.sushmoyr.ajkalnewyork.models.core.Photo
import com.sushmoyr.ajkalnewyork.models.core.News as CoreNews

sealed class DataModel {

    data class Advertisement(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("exp_date")
        val expDate: String = "",
        @SerializedName("position")
        val position: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("created_at")
        val createdAt: String = "",
        @SerializedName("updated_at")
        val updatedAt: String = ""

        ) : DataModel()

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
        @SerializedName("news_images")
        val newsImages: List<String> = listOf()
    ) : DataModel() {

        val defaultImage get() = "https://ajkal.fastrider.co$_defaultImage"

        fun toNews(): CoreNews {
            return CoreNews(
                id, categoryId, subcategoryId, countryId, divisionId, districtId, newsTitle,
                slug, description, _defaultImage, videoId, popularNews, status, createdBy,
                createdAt, newsImages
            )
        }
    }

    data class GalleryItem(
        val images: List<Photo>
    ) : DataModel()
}

