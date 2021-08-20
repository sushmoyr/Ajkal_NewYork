package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.core.BreakingNews
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.models.core.Photo
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

    @GET("news?sortBy=count&order=desc")
    suspend fun getTrendingNews(): Response<List<DataModel.News>>

    @GET("news?sortBy=count&order=desc")
    suspend fun getTrendingNews(
        @Query("categoryId") categoryId: Int
    ): Response<List<DataModel.News>>

    @GET("breakingNews")
    suspend fun getBreakingNews(): Response<List<BreakingNews>>

    @GET("news")
    suspend fun getNewsById(
        @Query("id") id: Int
    ):Response<List<News>>

}