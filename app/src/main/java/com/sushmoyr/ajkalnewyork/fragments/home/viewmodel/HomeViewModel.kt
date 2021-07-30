package com.sushmoyr.ajkalnewyork.fragments.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(private val repository: Repository) : ViewModel() {

    val allCategory = MutableLiveData<Response<List<Category>>>()

    init {
        getHomeItems()
    }

    fun getAllCats() {
        viewModelScope.launch(Dispatchers.IO) {
            allCategory.postValue(repository.getAllCategory())
        }
    }

    fun getAllNews() {
        viewModelScope.launch {
            val data = repository.getAllNews()
            if (data.isSuccessful) {
                val items = data.body()!!
                items.forEach { news ->
                    Log.d("newApi", news.toString())
                }
            }
        }
    }

    fun getAllAds() {
        viewModelScope.launch {
            val data = repository.getAllAds()
            if (data.isSuccessful) {
                val items = data.body()!!
                items.forEach { news ->
                    Log.d("ads", news.toString())
                }
            }
        }
    }

    val homeItems = MutableLiveData<List<DataModel>>()

    private fun getHomeItems() {
        viewModelScope.launch {
            val adsDeferred = async { repository.getAllAds() }
            val newsDeferred = async { repository.getAllNews() }

            val ads = adsDeferred.await()
            val news = newsDeferred.await()

            val homeItemList = mutableListOf<DataModel>()

            if (ads.isSuccessful && news.isSuccessful) {
                var advertisements = mutableListOf<DataModel.Advertisement>()
                if (ads.body() != null) {
                    advertisements.addAll(ads.body()!!)
                }
                advertisements = advertisements.shuffled() as MutableList<DataModel.Advertisement>

                val offset = 5
                var count = 0
                var adsIndex = 0

                news.body()!!.forEach {
                    if (count != 0 && count % offset == 0 && adsIndex < advertisements.size) {
                        homeItemList.add(advertisements[adsIndex++])
                        homeItemList.add(advertisements[adsIndex++])
                    } else {
                        homeItemList.add(it)
                    }
                    count++
                }

                homeItemList.forEach { dataModel ->
                    Log.d("recycler", dataModel.toString())
                }

                homeItems.postValue(homeItemList)

            }
        }
    }

}