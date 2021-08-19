package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.core.SuperUser
import com.sushmoyr.ajkalnewyork.models.utility.LoginRequest
import com.sushmoyr.ajkalnewyork.models.utility.LoginResponse
import com.sushmoyr.ajkalnewyork.models.utility.RegisterRequest
import com.sushmoyr.ajkalnewyork.models.utility.RegistrationResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("login")
    suspend fun loginUser(@Body user: LoginRequest): Response<LoginResponse>

    @GET("user")
    suspend fun authenticate(): Response<SuperUser>

    @GET("logout")
    suspend fun logout()

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<RegistrationResponse>
}