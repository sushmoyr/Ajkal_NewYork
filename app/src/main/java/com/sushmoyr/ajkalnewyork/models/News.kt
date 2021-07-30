package com.sushmoyr.ajkalnewyork.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class News(
    val breakingId: String,
    val categoryId: Int,
    val countryId: Int,
    val createdBy: String,
    val createdDate: String,
    val defaultImage: String,
    val description: String,
    val districtId: Int,
    val divisionId: Int,
    val id: String,
    val isArchived: Boolean,
    val newsTitle: String,
    val seoDescription: String,
    val seoKeyword: String,
    val status: String,
    val subCategoryId: Int,
    val videoLink: String
): Parcelable

data class CategoryItem(
    val categoryId: Int,
    val categoryTitleEn: String,
    val categoryTitleBn: String,
)
