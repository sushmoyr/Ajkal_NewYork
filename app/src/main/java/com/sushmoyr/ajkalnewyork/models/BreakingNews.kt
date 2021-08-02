package com.sushmoyr.ajkalnewyork.models

data class BreakingNews(
    val bnews_title: String,
    val created_at: String,
    val deleted_at: String,
    val id: Int,
    val news_id: Int,
    val status: String,
    val updated_at: String
)