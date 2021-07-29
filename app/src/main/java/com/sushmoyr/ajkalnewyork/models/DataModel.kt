package com.sushmoyr.ajkalnewyork.models

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
    ) : DataModel()
}

