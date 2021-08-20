package com.sushmoyr.ajkalnewyork.repository

import android.util.Log
import com.sushmoyr.ajkalnewyork.datasource.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.AdPostResponse
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.core.Video
import com.sushmoyr.ajkalnewyork.models.core.*
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds
import com.sushmoyr.ajkalnewyork.models.stripe.AdvertisementPayment
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.models.utility.LoginRequest
import com.sushmoyr.ajkalnewyork.models.utility.LoginResponse
import com.sushmoyr.ajkalnewyork.models.utility.RegisterRequest
import com.sushmoyr.ajkalnewyork.models.utility.RegistrationResponse
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

    suspend fun getAllNewsCore(): Response<List<News>>{
        Log.d("searchState", "Api Called")
        return RetrofitInstance.api.getAllNewsCore()
    }

    suspend fun getAllNews(categoryId: String?): Response<List<DataModel.News>> {
        Log.d("CallApi", "get all news by id")
        return RetrofitInstance.api.getAllNews(categoryId)
    }

    suspend fun getAllAds(): Response<List<DataModel.Advertisement>> {
        Log.d("CallApi", "get all ads")
        return RetrofitInstance.api.getAllAds()
    }
    suspend fun getAdSizes(): Response<AdvertisementSize> {
        Log.d("CallApi", "get all ads")
        return RetrofitInstance.api.getAdSizes()
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

    suspend fun getAllSubCategory(): Response<List<SubCategory>> {
        return RetrofitInstance.api.getAllSubCategory()
    }

    suspend fun getAllVideos(): Response<List<Video>> {
        return RetrofitInstance.api.getAllVideos()
    }

    suspend fun postAdvertisement(advertisement: SponsoredAds): Response<AdPostResponse> {
        Log.d("upload", "api upload called")
        return RetrofitInstance.api.postAdvertisement(advertisement)
    }

    suspend fun getAllDivision(): Response<List<Division>> {
        return RetrofitInstance.api.getAllDivision()
    }

    suspend fun getDistrictByDivision(divisionId: String): Response<List<District>> {
        return RetrofitInstance.api.getDistrictByDivision(divisionId)
    }

    suspend fun getBdNews(divisionId: String, districtId: String): Response<List<DataModel.News>> {
        return RetrofitInstance.api.getBdNews(divisionId, districtId)
    }

    suspend fun getBdNews(divisionId: String): Response<List<DataModel.News>> {
        return RetrofitInstance.api.getBdNews(divisionId)
    }

    suspend fun getBdNews(): Response<List<DataModel.News>> {
        return RetrofitInstance.api.getBdNews()
    }

    suspend fun postAdPayment(adPayment: AdvertisementPayment) {
        RetrofitInstance.api.postAdPayment(adPayment)
    }


    //handle user login registration
    suspend fun loginUser(user: LoginRequest): Response<LoginResponse> {
        return RetrofitInstance.authApi.loginUser(user)
    }

    suspend fun authUser() {
        RetrofitInstance.authApi.authenticate()
    }

    suspend fun logout() {
       RetrofitInstance.authApi.logout()
    }

    suspend fun register(request: RegisterRequest): Response<RegistrationResponse> {
        return RetrofitInstance.authApi.register(request)
    }


}