package com.sushmoyr.ajkalnewyork.models

data class News(
    val title: String,
    val category: Category,
    val image: String,
)

data class Category(
    val categoryId: Int,
    val categoryTitleEn: String,
    val categoryTitleBn: String,
)
