package com.sushmoyr.ajkalnewyork.repository

import com.sushmoyr.ajkalnewyork.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.models.Photo
import retrofit2.Response

class Repository {
    suspend fun getAllCategory(): Response<List<Category>>{
        return RetrofitInstance.api.getAllCategory()
    }

    suspend fun getAllNews(): Response<List<DataModel.News>> {
        return RetrofitInstance.mockApi.getAllNews()
    }

    suspend fun getAllNews(categoryId: Int): Response<List<DataModel.News>> {
        return RetrofitInstance.mockApi.getAllNews(categoryId)
    }

    suspend fun getAllAds(): Response<List<DataModel.Advertisement>> {
        return RetrofitInstance.mockApi.getAllAds()
    }

    suspend fun getPhotos(): Response<List<Photo>> {
        return RetrofitInstance.mockApi2.getPhotoGallery(1, 5)
    }

    suspend fun getFullGallery(): Response<List<Photo>>{
        return RetrofitInstance.mockApi2.getPhotoGallery()
    }


}