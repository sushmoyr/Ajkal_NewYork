package com.sushmoyr.ajkalnewyork.repository


import android.util.Log
import com.sushmoyr.ajkalnewyork.NetworkResponse
import com.sushmoyr.ajkalnewyork.datasource.api.RetrofitInstance
import com.sushmoyr.ajkalnewyork.models.AdPostResponse
import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.core.*
import com.sushmoyr.ajkalnewyork.models.core.ads.AdvertisementSize
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds
import com.sushmoyr.ajkalnewyork.models.core.locations.Location
import com.sushmoyr.ajkalnewyork.models.stripe.AdvertisementPayment
import com.sushmoyr.ajkalnewyork.models.stripe.PaymentIntentModel
import com.sushmoyr.ajkalnewyork.models.utility.*
import com.sushmoyr.ajkalnewyork.models.utility.transactionhistory.TransactionHistory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import retrofit2.http.GET

class RemoteDataSource {
    suspend fun getAllCategory(): NetworkResponse<Response<List<Category>>> {
        return try{
            NetworkResponse.Success(RetrofitInstance.api.getAllCategory())
        }catch (e: Exception) {
            Log.d("ErrorPoint", "At cat api")
            e.printStackTrace()
            NetworkResponse.Error(e)
        }
    }

    suspend fun getAllNews(): NetworkResponse<Response<List<DataModel.News>>> {
        Log.d("CallApi", "get all news by id")
        return try{
            NetworkResponse.Success(RetrofitInstance.api.getAllNews())
        }catch (e: Exception) {
            Log.d("ErrorPoint", "At AllNews api")
            e.printStackTrace()
            NetworkResponse.Error(e)
        }
    }

    suspend fun getAllNewsCore(): Response<List<News>> {
        Log.d("searchState", "Api Called")
        return RetrofitInstance.api.getAllNewsCore()
    }

    suspend fun getAllNews(categoryId: String?): NetworkResponse<Response<List<DataModel.News>>> {
        Log.d("CallApi", "get all news by id")
        return try{
            if(categoryId != null)
                NetworkResponse.Success(RetrofitInstance.api.getAllNews())
            else
                NetworkResponse.Success(RetrofitInstance.api.getAllNews(categoryId))
        }catch (e: Exception) {
            Log.d("ErrorPoint", "At AllNews by id api")
            e.printStackTrace()
            NetworkResponse.Error(e)
        }
    }

    suspend fun getAllAds(): Response<List<DataModel.Advertisement>> {
        Log.d("CallApi", "get all ads")
        return try {
            RetrofitInstance.api.getAllAds()
        }catch (e: Exception) {
            Log.d("ErrorPoint", "At cat api")
            e.printStackTrace()
            val value = emptyList<DataModel.Advertisement>()
            Response.error(400, value.toString().toResponseBody("application/json".toMediaTypeOrNull()))
        }
    }

    suspend fun getAdSizes(): Response<AdvertisementSize> {
        Log.d("CallApi", "get all ads")
        return RetrofitInstance.api.getAdSizes()
    }


    suspend fun getPhotos(): NetworkResponse<Response<List<Photo>>> {
        Log.d("CallApi", "get all photos")
        return try {
            NetworkResponse.Success(RetrofitInstance.api.getPhotoGallery(1, 5))
        }catch (e: Exception){
            NetworkResponse.Error(e)
        }
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

    suspend fun getBreakingNews(): NetworkResponse<Response<List<BreakingNews>>> {
        Log.d("CallApi", "get breaking news")
        return try {
            NetworkResponse.Success(RetrofitInstance.api.getBreakingNews())
        }catch (e: Exception){
            NetworkResponse.Error(e)
        }
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

    suspend fun getAllSubCategory(): NetworkResponse<Response<List<SubCategory>>> {
        return try {
            NetworkResponse.Success(RetrofitInstance.api.getAllSubCategory())
        }catch (e: Exception){
            NetworkResponse.Error(e)
        }
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

    suspend fun uploadSponsoredAd(
        userId: RequestBody,
        adTitle: RequestBody,
        adLink: RequestBody,
        sizeId: RequestBody,
        adImage: MultipartBody.Part,
        createdDate: RequestBody,
        expDate: RequestBody,
        forDay: RequestBody,
        amount: RequestBody,
        status: RequestBody,
        createdAt: RequestBody,
        updatedAt: RequestBody
    ): Response<UploadResponse> {
        return RetrofitInstance.api.uploadSponsoredAd(
            userId,
            adTitle,
            adLink,
            sizeId,
            adImage,
            createdDate,
            expDate,
            forDay,
            amount,
            status,
            createdAt,
            updatedAt
        )
    }

    suspend fun getUserSponsoredAds(userId: String): Response<List<SponsoredAds>> {
        return RetrofitInstance.api.getUserSponsoredAds(userId)
    }

    suspend fun getSessionGeoLocationInfo():Response<GeoLocationInfo> {
        return RetrofitInstance.geoApi.getSessionGeoLocationInfo()
    }

    suspend fun postTransactionInfo(transactionInfo: TransactionInfo) {
        RetrofitInstance.api.postTransactionInfo(transactionInfo)
    }

    suspend fun updateSponsoredAd(
        adId: String,
        userId: RequestBody,
        adTitle: RequestBody,
        adLink: RequestBody,
        sizeId: RequestBody,
        adImage: MultipartBody.Part,
        createdDate: RequestBody,
        expDate: RequestBody,
        forDay: RequestBody,
        amount: RequestBody,
        status: RequestBody,
        createdAt: RequestBody,
        updatedAt: RequestBody
    ): Response<UploadResponse> {
        return RetrofitInstance.api.updateSponsoredAd(
            adId,
            userId,
            adTitle,
            adLink,
            sizeId,
            adImage,
            createdDate,
            expDate,
            forDay,
            amount,
            status,
            createdAt,
            updatedAt
        )
    }

    suspend fun getTransactionHistory(userId: String): Response<TransactionHistory> {
        return RetrofitInstance.api.getTransactionHistory(userId)
    }


    suspend fun deleteAd(adId: String): Response<UploadResponse> {
        return RetrofitInstance.api.deleteAd(adId)
    }

    suspend fun updateUser(id: String, requestBody: ProfileUpdateRequest):Response<UploadResponse> {
        return RetrofitInstance.authApi.updateUser(id, requestBody)
    }

    suspend fun uploadProfileImage(id: Int, profileImage: MultipartBody.Part):Response<UploadResponse> {
        return RetrofitInstance.authApi.uploadProfileImage(id, profileImage)
    }

    suspend fun updatePassword(id: String, request: UpdatePasswordRequest):
            Response<UploadResponse> {
        return RetrofitInstance.authApi.updatePassword(id, request)
    }

    suspend fun getArchivedNews(): NetworkResponse<List<News>> {
        return try {
            NetworkResponse.Success(RetrofitInstance.api.getArchivedNews())
        }catch (e: Exception){
            NetworkResponse.Error(e)
        }
    }

    suspend fun getAllLocations(): NetworkResponse<Location> {
        return try{
            NetworkResponse.Success(RetrofitInstance.api.getAllLocations())
        }
        catch (e: Exception){
            NetworkResponse.Error(e)
        }
    }

    suspend fun getLocalNews(categoryId: String, divisionId: String, districtId: String):
            NetworkResponse<List<News>>{
        return try {
            NetworkResponse.Success(RetrofitInstance.api.getLocalNews(categoryId, divisionId, districtId))
        }
        catch (e: Exception){
            NetworkResponse.Error(e)
        }
    }


}