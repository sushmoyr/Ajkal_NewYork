package com.sushmoyr.ajkalnewyork.datasource.api

import com.sushmoyr.ajkalnewyork.models.AdPostResponse
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.core.Video
import com.sushmoyr.ajkalnewyork.models.core.*
import com.sushmoyr.ajkalnewyork.models.core.ads.AdvertisementSize
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds
import com.sushmoyr.ajkalnewyork.models.stripe.AdvertisementPayment
import com.sushmoyr.ajkalnewyork.models.utility.TransactionInfo
import com.sushmoyr.ajkalnewyork.models.utility.transactionhistory.TransactionHistory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AjkalApi {

    @GET("news.php")
    suspend fun getAllNews(): Response<List<DataModel.News>>

    @GET("news.php")
    suspend fun getAllNewsCore(): Response<List<News>>

    @GET("news.php")
    suspend fun getAllNews(
        @Query("category_id") categoryId: String?
    ): Response<List<DataModel.News>>

    @GET("category.php")
    suspend fun getAllCategory(): Response<List<Category>>


    @GET("advertisement.php")
    suspend fun getAllAds(): Response<List<DataModel.Advertisement>>

    @GET("adSizes.php")
    suspend fun getAdSizes(): Response<AdvertisementSize>

    @GET("photos.php")
    suspend fun getPhotoGallery(): Response<List<Photo>>

    @GET("photos.php")
    suspend fun getPhotoGallery(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<Photo>>

    @GET("news.php?sortBy=count&order=desc")
    suspend fun getTrendingNews(): Response<List<DataModel.News>>

    @GET("news.php?sortBy=count&order=desc")
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

    @GET("subcategory.php")
    suspend fun getAllSubCategory(): Response<List<SubCategory>>

    @GET("videos.php")
    suspend fun getAllVideos(): Response<List<Video>>

    @POST("advertisement.php")
    suspend fun postAdvertisement(@Body advertisement: SponsoredAds): Response<AdPostResponse>

    @Multipart
    @POST("UploadApi.php?apicall=upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>

    @GET("division.php")
    suspend fun getAllDivision(): Response<List<Division>>

    @GET("districts.php")
    suspend fun getDistrictByDivision(
        @Query("division_id") divisionId: String): Response<List<District>>


    @GET("bd_news.php")
    suspend fun getBdNews(
        @Query("division_id") divisionId: String,
        @Query("district_id") districtId: String
    ):Response<List<DataModel.News>>

    @GET("bd_news.php")
    suspend fun getBdNews(
        @Query("division_id") divisionId: String
    ):Response<List<DataModel.News>>

    @GET("bd_news.php")
    suspend fun getBdNews():Response<List<DataModel.News>>

    @POST("adPayments.php")
    suspend fun postAdPayment(@Body adPayment: AdvertisementPayment)

    @Multipart
    @POST("ad/upload")
    suspend fun uploadSponsoredAd(
        @Part("user_id")
        userId: RequestBody,
        @Part("ad_title")
         adTitle: RequestBody,
        @Part("ad_link")
         adLink: RequestBody,
        @Part("size_id")
         sizeId: RequestBody,
        @Part
        adImage: MultipartBody.Part,
        @Part("created_date")
         createdDate: RequestBody,
        @Part("exp_date")
         expDate: RequestBody,
        @Part("for_day")
         forDay: RequestBody,
        @Part("amount")
         amount: RequestBody,
        @Part("status")
         status: RequestBody,
        @Part("created_at")
         createdAt: RequestBody,
        @Part("updated_at")
         updatedAt: RequestBody
    ):Response<UploadResponse>

    @GET("sponsored_ads.php")
    suspend fun getUserSponsoredAds(@Query("user_id") userId: String):Response<List<SponsoredAds>>

    @Multipart
    @POST("ad/update/{id}")
    suspend fun updateSponsoredAd(
        @Path("id") adId: String,
        @Part("user_id")
        userId: RequestBody,
        @Part("ad_title")
        adTitle: RequestBody,
        @Part("ad_link")
        adLink: RequestBody,
        @Part("size_id")
        sizeId: RequestBody,
        @Part
        adImage: MultipartBody.Part,
        @Part("created_date")
        createdDate: RequestBody,
        @Part("exp_date")
        expDate: RequestBody,
        @Part("for_day")
        forDay: RequestBody,
        @Part("amount")
        amount: RequestBody,
        @Part("status")
        status: RequestBody,
        @Part("created_at")
        createdAt: RequestBody,
        @Part("updated_at")
        updatedAt: RequestBody
    ):Response<UploadResponse>

    @POST("ad/payment/stripe")
    suspend fun postTransactionInfo(@Body transactionInfo: TransactionInfo)

    @GET("payment/history/{id}")
    suspend fun getTransactionHistory(@Path("id") userId: String):
            Response<TransactionHistory>

    @GET("ad/destroy/{id}")
    suspend fun deleteAd(@Path("id") id: String): Response<UploadResponse>
}