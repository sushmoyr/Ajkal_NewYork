package com.sushmoyr.ajkalnewyork.fragments.trending

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TrendingViewModel(private val _repository: Repository) : ViewModel() {

    val trendingItems = MutableLiveData<List<DataModel>>()
    private val repository = _repository.remoteDataSource

    fun getTrendingNews(categoryId: Int? = null) {
        viewModelScope.launch {
            val adsDeferred = async { repository.getAllAds() }
            val newsDeferred = async {
                when (categoryId) {
                    null -> repository.getTrendingNews()
                    else -> repository.getTrendingNews(categoryId)
                }
            }

            val ads = adsDeferred.await()
            val newsResponse = newsDeferred.await()

            val homeItemList = mutableListOf<DataModel>()

            if (ads.isSuccessful && newsResponse.isSuccessful) {
                var advertisements = mutableListOf<DataModel.Advertisement>()
                if (ads.body() != null) {
                    advertisements.addAll(ads.body()!!)
                }
                advertisements = advertisements.shuffled() as MutableList<DataModel.Advertisement>

                val offset = 5
                var count = 0
                var adsIndex = 0
                val adCount = 2
                val newsList = newsResponse.body()!!.toMutableList()
                newsList.sortByDescending { it.createdAt }
                val news = newsList.filter { it.popularNews == "1" }
                news.forEach {
                    Log.d("Final", it.defaultImage)
                    if (count != 0 && count % offset == 0 && adsIndex < advertisements.size) {
                        for (j in 0 until adCount) {
                            if (adsIndex < advertisements.size) {
                                homeItemList.add(advertisements[adsIndex++])
                                if (adsIndex == advertisements.size) {
                                    adsIndex = 0
                                }
                            }
                        }
                    } else {
                        homeItemList.add(it)
                    }
                    count++
                }

                trendingItems.postValue(homeItemList)

            }
        }
    }
}