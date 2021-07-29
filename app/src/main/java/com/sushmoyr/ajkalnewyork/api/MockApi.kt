package com.sushmoyr.ajkalnewyork.api

import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel
import retrofit2.Response
import retrofit2.http.GET

interface MockApi {

    @GET("news")
    suspend fun getAllNews(): Response<List<DataModel.News>>

    @GET("Advertisement")
    suspend fun getAllAds(): Response<List<DataModel.Advertisement>>

}