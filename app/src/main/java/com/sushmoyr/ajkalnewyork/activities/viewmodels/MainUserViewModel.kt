package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.core.AdvertisementSize
import com.sushmoyr.ajkalnewyork.models.utility.User
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.launch

class MainUserViewModel: ViewModel() {

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

    private val _adSizes = MutableLiveData<AdvertisementSize>()
    val adSizes get() = _adSizes
    var lastFetchedAdSizeData: AdvertisementSize = AdvertisementSize()
    var selectedItemPosition: Int = 0

    private fun getAdSizes(){
        viewModelScope.launch {
            val response = repository.remoteDataSource.getAdSizes()
            if(response.isSuccessful){
                val data = response.body()!!
                Log.d("response", response.body()!!.size.toString())
                _adSizes.postValue(data)
            }
        }
    }

    val uploadedImageUri = MutableLiveData<Uri>()

}