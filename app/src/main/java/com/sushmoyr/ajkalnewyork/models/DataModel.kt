package com.sushmoyr.ajkalnewyork.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.sushmoyr.ajkalnewyork.models.News

sealed class DataModel {
    class Advertisement(
        val advType: Int,
        val createdDate: String,
        val expDate: String,
        val id: String,
        val imagePath: String,
        val paymentId: String,
        val userId: Int
    ) : DataModel()

    data class News(
        val breakingId: String,
        val categoryId: Int,
        val countryId: Int,
        val createdBy: String,
        val createdDate: String,
        val defaultImage: String,
        val description: String,
        val districtId: Int,
        val divisionId: Int,
        val id: Int,
        val isArchived: Boolean,
        val newsTitle: String,
        val seoDescription: String,
        val seoKeyword: String,
        val status: String,
        val subCategoryId: Int,
        val videoLink: String,
        val count: Int
    ) : DataModel(){
        fun toNews(): com.sushmoyr.ajkalnewyork.models.News{
            return com.sushmoyr.ajkalnewyork.models.News(
                breakingId, categoryId, countryId, createdBy, createdDate, defaultImage,
                description, districtId, divisionId, id, isArchived, newsTitle, seoDescription,
                seoKeyword, status, subCategoryId, videoLink, count
            )
        }
    }

    data class GalleryItem(
        val images: List<Photo>
    ) : DataModel()
}

