package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.core.ads.AdvertisementSize
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.models.utility.transactionhistory.TransactionHistory
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class MainUserViewModel : ViewModel() {

    private val repository: Repository by lazy {
        Repository()
    }

    private val _currentUser = MutableLiveData<User>().also {
        it.value = null
    }
    val currentUser get() = _currentUser
    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    init {
        getAdSizes()
    }



    private val _loader = MutableLiveData<Boolean>()
    val loader get() = _loader

    private val _adSizes = MutableLiveData<AdvertisementSize>()
    val adSizes get() = _adSizes
    var lastFetchedAdSizeData: AdvertisementSize = AdvertisementSize()
    var selectedItemPosition: Int? = null
    var selectedDateMillis: Long? = null

    private fun getAdSizes() {
        viewModelScope.launch {
            val response = repository.remoteDataSource.getAdSizes()
            if (response.isSuccessful) {
                val data = response.body()!!
                Log.d("response", response.body()!!.size.toString())
                _adSizes.postValue(data)
                lastFetchedAdSizeData = data
            }
        }
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
        return repository
            .remoteDataSource
            .uploadSponsoredAd(
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


    /**
     * User Advertisement loading section
     * Api calls and Loaders
     */

    private val _userSponsoredAds = MutableLiveData<List<SponsoredAds>>()
    val userSponsoredAds get() = _userSponsoredAds

    private val _userAdLoader = MutableLiveData<Boolean>().apply {
        value = false
    }
    val userAdLoader get() = _userAdLoader

    fun getUserSponsoredAds(userId: String){
        viewModelScope.launch {
            _userAdLoader.value = true
            val responseAsync = async { repository.remoteDataSource.getUserSponsoredAds(userId) }
            val response = responseAsync.await()
            if(response.isSuccessful){
                _userAdLoader.value = false
                _userSponsoredAds.postValue(response.body()!!)
            }
            else{
                _userAdLoader.value = false
            }

        }
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

        return repository
            .remoteDataSource
            .updateSponsoredAd(
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

    private val _transactionHistory = MutableLiveData<TransactionHistory>()
    val transactionHistory = _transactionHistory

    fun getTransactionHistory(userId: String){
        Log.d("history", userId)
        viewModelScope.launch {
            val response: Response<TransactionHistory> = repository.remoteDataSource.getTransactionHistory(userId)
            if(response.isSuccessful){
                _transactionHistory.postValue(response.body())
                println(response.body())
            }
        }
    }

    suspend fun deleteAd(ad: SponsoredAds): Response<UploadResponse> = repository.remoteDataSource
    .deleteAd(ad.id.toString())


}