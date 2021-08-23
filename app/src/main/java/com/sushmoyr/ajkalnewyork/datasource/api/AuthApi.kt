package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.core.SuperUser
import com.sushmoyr.ajkalnewyork.models.utility.*
import okhttp3.MultipartBody
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

    @POST("update/profile/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body requestBody: ProfileUpdateRequest):
            Response<UploadResponse>

    @Multipart
    @POST("change/profile/picture/{id}")
    suspend fun uploadProfileImage(@Path("id") id: Int, @Part profileImage: MultipartBody.Part):
            Response<UploadResponse>

    @POST("update/password/{id}")
    suspend fun updatePassword(
        @Path("id") id: String,
        @Body request: UpdatePasswordRequest): Response<UploadResponse>
}