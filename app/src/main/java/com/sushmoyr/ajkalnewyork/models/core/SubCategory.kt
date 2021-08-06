package com.sushmoyr.ajkalnewyork.models.core


import com.google.gson.annotations.SerializedName

data class SubCategory(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("category_id")
    val categoryId: String = "",
    @SerializedName("subcategory_name")
    val subcategoryName: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)