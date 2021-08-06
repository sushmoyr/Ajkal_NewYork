package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.models.User
import com.sushmoyr.ajkalnewyork.models.core.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AjkalApi {

    @GET("news.php")
    suspend fun getAllNews(): Response<List<DataModel.News>>

    @GET("news.php")
    suspend fun getAllNews(
        // FIXME: 8/6/2021
        @Query("category_id") categoryId: String?
    ): Response<List<DataModel.News>>

    @GET("category.php")
    suspend fun getAllCategory(): Response<List<Category>>


    @GET("advertisement.php")
    suspend fun getAllAds(): Response<List<DataModel.Advertisement>>

    @GET("photos.php")
    suspend fun getPhotoGallery(): Response<List<Photo>>

    @GET("photos.php")
    suspend fun getPhotoGallery(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<Photo>>

    @GET("news?sortBy=count&order=desc")
    suspend fun getTrendingNews(): Response<List<DataModel.News>>

    @GET("news?sortBy=count&order=desc")
    suspend fun getTrendingNews(
        @Query("categoryId") categoryId: Int
    ): Response<List<DataModel.News>>

    @GET("breaking_news.php")
    suspend fun getBreakingNews(): Response<List<BreakingNews>>

    @GET("news.php")
    suspend fun getNewsById(
        @Query("id") id: String?
    ): Response<List<News>>

    @GET("user.php")
    suspend fun getUserById(@Query("userid") id: String): Response<List<SuperUser>>

}