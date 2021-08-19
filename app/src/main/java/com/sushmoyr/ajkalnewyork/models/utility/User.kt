package com.sushmoyr.ajkalnewyork.models.utility


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("role")
    val role: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("provider_id")
    val providerId: String? = null,
    @SerializedName("provider")
    val provider: String? = null,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,
    @SerializedName("mobile")
    val mobile: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("last_seen")
    val lastSeen: String? = null,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)