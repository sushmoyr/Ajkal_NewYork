package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.models.News
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewsDetailViewModel(private val repository: Repository): ViewModel() {

    var advertisements = MutableLiveData<List<DataModel.Advertisement>>()
    var news = MutableLiveData<List<DataModel.News>>()
    val displayNews = MutableLiveData<News>()

    init {
        getNews()
        getAdvertisements()
    }

    private fun getAdvertisements(){
        viewModelScope.launch {
            val adsDeferred = async { repository.getAllAds() }
            val adsData = adsDeferred.await()
            if(adsData.isSuccessful){
                if(!adsData.body().isNullOrEmpty()){
                    advertisements.postValue(adsData.body())
                }
            }
        }
    }

    private fun getNews(){
        viewModelScope.launch {
            val newsDeferred = async { repository.getAllNews() }
            val newsData = newsDeferred.await()
            if(newsData.isSuccessful){
                if(!newsData.body().isNullOrEmpty()){
                    news.postValue(newsData.body())
                }
            }
        }
    }

    fun setDisplayNews(news: News){
        displayNews.postValue(news)
    }
}