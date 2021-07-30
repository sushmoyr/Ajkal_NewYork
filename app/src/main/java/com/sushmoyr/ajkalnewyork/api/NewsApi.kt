package com.sushmoyr.ajkalnewyork.api

import com.sushmoyr.ajkalnewyork.models.Category
import retrofit2.Response
import retrofit2.http.GET

interface NewsApi {

    @GET("Category")
    suspend fun getAllCategory(): Response<List<Category>>

}