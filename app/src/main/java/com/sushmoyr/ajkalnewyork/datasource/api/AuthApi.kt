package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.utility.LoginRequest
import com.sushmoyr.ajkalnewyork.models.utility.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("login")
    suspend fun loginUser(@Body user: LoginRequest): Response<LoginResponse>
}