package com.sushmoyr.ajkalnewyork.models.core


import com.google.gson.annotations.SerializedName

data class SuperUser(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("role")
    val role: String = "",
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
    @SerializedName("password")
    val password: String = "",
    @SerializedName("last_seen")
    val lastSeen: String = "",
    @SerializedName("remember_token")
    val rememberToken: Any? = null,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
)