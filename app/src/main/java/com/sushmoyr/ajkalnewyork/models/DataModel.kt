package com.sushmoyr.ajkalnewyork.models

import com.google.gson.annotations.SerializedName
import com.sushmoyr.ajkalnewyork.models.core.Photo
import com.sushmoyr.ajkalnewyork.models.core.News as CoreNews

sealed class DataModel {

    data class Advertisement(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("user_id")
        val userId: String? = null,
        @SerializedName("payment_id")
        val paymentId: Any? = null,
        @SerializedName("ad_title")
        val adTitle: String = "",
        @SerializedName("ad_link")
        val adLink: String = "",
        @SerializedName("ad_image")
        val _adImage: String = "",
        @SerializedName("exp_date")
        val expDate: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("created_at")
        val createdAt: String = "",
        @SerializedName("updated_at")
        val updatedAt: String = "",
        @SerializedName("size_id")
        val sizeId: String = "",
        @SerializedName("created_date")
        val createdDate: String = "",
        @SerializedName("for_day")
        val forDay: String = "",
        @SerializedName("amount")
        val amount: String = "",

        ) : DataModel() {
        val adImage get() = "https://ajkal.fastrider.co$_adImage"
    }

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
    ) : DataModel() {

        val defaultImage get() = "https://ajkal.fastrider.co$_defaultImage"

        fun toNews(): CoreNews {
            return CoreNews(
                id, categoryId, subcategoryId, countryId, divisionId, districtId, newsTitle,
                slug, description, _defaultImage, videoId, popularNews, status, createdBy, createdAt
            )
        }
    }

    data class GalleryItem(
        val images: List<Photo>
    ) : DataModel()
}

