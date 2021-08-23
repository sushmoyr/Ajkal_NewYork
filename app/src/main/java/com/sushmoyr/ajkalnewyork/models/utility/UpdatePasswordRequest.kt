package com.sushmoyr.ajkalnewyork.models.utility

data class UpdatePasswordRequest(
    val new_password: String,
    val password_confirmation: String,
    val current_password: String
)
