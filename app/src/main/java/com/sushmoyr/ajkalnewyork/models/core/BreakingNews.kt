package com.sushmoyr.ajkalnewyork.models.core

import com.google.gson.annotations.SerializedName

data class BreakingNews(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("news_id")
    val newsId: String = "",
    @SerializedName("bnews_title")
    val bnewsTitle: String = "",
    @SerializedName("status")
    val status: String = "")