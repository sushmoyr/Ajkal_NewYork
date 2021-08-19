package com.sushmoyr.ajkalnewyork.models.utility

import com.google.gson.annotations.SerializedName

sealed class LoginResponse{
    /**
     * on login success response ->
     * "token": string token
     *
     * on login failure response
     * "error": String = "Unauthorised"
     */

    data class Success(
        @SerializedName("token")
        val token: String = ""
    ):LoginResponse()

    data class Error(
        @SerializedName("error")
        val error: String = ""
    ):LoginResponse()
}
