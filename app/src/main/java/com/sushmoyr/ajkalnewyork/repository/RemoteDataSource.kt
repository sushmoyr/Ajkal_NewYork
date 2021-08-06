package com.sushmoyr.ajkalnewyork.repository

import android.util.Log
import com.sushmoyr.ajkalnewyork.datasource.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.models.core.*
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import retrofit2.Response

class RemoteDataSource {
    suspend fun getAllCategory(): Response<List<Category>> {
        Log.d("CallApi", "get all category")
        return RetrofitInstance.api.getAllCategory()
    }

    suspend fun getAllNews(): Response<List<DataModel.News>> {
        Log.d("CallApi", "get all news by id")
        return RetrofitInstance.api.getAllNews()
    }

    suspend fun getAllNews(categoryId: String?): Response<List<DataModel.News>> {
        Log.d("CallApi", "get all news by id")
        return RetrofitInstance.api.getAllNews(categoryId)
    }

    suspend fun getAllAds(): Response<List<DataModel.Advertisement>> {
        Log.d("CallApi", "get all ads")
        return RetrofitInstance.api.getAllAds()
    }

    suspend fun getPhotos(): Response<List<Photo>> {
        Log.d("CallApi", "get all photos")
        return RetrofitInstance.api.getPhotoGallery(1, 5)
    }

    suspend fun getFullGallery(): Response<List<Photo>> {
        Log.d("CallApi", "get full gallery")
        return RetrofitInstance.api.getPhotoGallery()
    }

    suspend fun getTrendingNews(categoryId: Int): Response<List<DataModel.News>> {
        Log.d("CallApi", "get trending news")
        return RetrofitInstance.api.getTrendingNews(categoryId)
    }

    suspend fun getTrendingNews(): Response<List<DataModel.News>> {
        Log.d("CallApi", "get trending by id")
        return RetrofitInstance.api.getTrendingNews()
    }

    suspend fun getBreakingNews(): Response<List<BreakingNews>> {
        Log.d("CallApi", "get breaking news")
        return RetrofitInstance.api.getBreakingNews()
    }

    suspend fun getNewsById(newsId: String?): Response<List<News>> {
        Log.d("CallApi", "get news by id")
        return RetrofitInstance.api.getNewsById(newsId)
    }

    suspend fun createPaymentIntent(
        paymentMethodType: String,
        item: PaymentIntentModel
    ): Response<PaymentIntentModel> {
        Log.d("CallApi", "Stripe payment intent")
        return RetrofitInstance.stripeApi.createPaymentIntent(item)
    }

    suspend fun getUser(createdBy: String): Response<List<SuperUser>> {
        return RetrofitInstance.api.getUserById(createdBy)
    }

}