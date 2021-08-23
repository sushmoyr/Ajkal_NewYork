package com.sushmoyr.ajkalnewyork.models.utility

data class ProfileUpdateRequest(
    val name: String,
    val email: String,
    val mobile: String,
    val address: String
)
