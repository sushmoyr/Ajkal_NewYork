package com.sushmoyr.ajkalnewyork.models

data class News(
    val title: String,
    val categoryItem: CategoryItem,
    val image: String,
)

data class CategoryItem(
    val categoryId: Int,
    val categoryTitleEn: String,
    val categoryTitleBn: String,
)
