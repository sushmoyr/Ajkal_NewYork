package com.sushmoyr.ajkalnewyork.api

import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.models.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MockApi {

    @GET("news")
    suspend fun getAllNews(): Response<List<DataModel.News>>

    @GET("news")
    suspend fun getAllNews(
        @Query("categoryId") categoryId: Int): Response<List<DataModel.News>>


    @GET("Advertisement")
    suspend fun getAllAds(): Response<List<DataModel.Advertisement>>

    @GET("galleryImages")
    suspend fun getPhotoGallery(): Response<List<Photo>>

    @GET("galleryImages")
    suspend fun getPhotoGallery(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ):Response<List<Photo>>

}