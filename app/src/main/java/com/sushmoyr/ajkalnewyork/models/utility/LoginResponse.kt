package com.sushmoyr.ajkalnewyork.models.utility

import com.google.gson.annotations.SerializedName
import com.sushmoyr.ajkalnewyork.models.core.SuperUser

/*sealed class LoginResponse{
    *//**
     * on login success response ->
     * "token": string token
     *
     * on login failure response
     * "error": String = "Unauthorised"
     *//*

    data class Success(
        @SerializedName("user")
        val user: SuperUser
    ):LoginResponse()

    data class Error(
        @SerializedName("error")
        val error: String = ""
    ):LoginResponse()
}*/

data class LoginResponse(
    @SerializedName("user")
    val user: User = User(),
)
