package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.models.core.SuperUser
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewsDetailViewModel(private val repository: RemoteDataSource): ViewModel() {

    var advertisements = MutableLiveData<List<DataModel.Advertisement>>()
    private val _news = MutableLiveData<List<DataModel.News>>()
    val news get() = _news
    val displayNews = MutableLiveData<News>()

    private val _newsCategory = MutableLiveData<Category>()
    val category get() = _newsCategory

    private val _allCategory = MutableLiveData<List<Category>>()
    val allCategory get() = _allCategory


    init {
        getAllCategory()
        getNews()
        getAdvertisements()
    }

    private fun getAllCategory() {
        viewModelScope.launch {
            val deferredResponse = async { repository.getAllCategory() }
            val response = deferredResponse.await()
            if(response.isSuccessful){
                if(!response.body().isNullOrEmpty()){
                    _allCategory.postValue(response.body())
                }
            }
        }
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

    fun getCategories(catId: String){
        viewModelScope.launch {
            val response = repository.getAllCategory()
            if(response.isSuccessful){
                val data = response.body()!!
                data.forEach { category ->
                    if(category.id == catId){
                        _newsCategory.postValue(category)
                        return@forEach
                    }
                }
            }
        }
    }

    fun setDisplayNews(news: News){
        displayNews.postValue(news)
    }

    private val _createdByUser = MutableLiveData<SuperUser>()
    val createdBy get() = _createdByUser

    fun getUser(createdBy: String) {
        viewModelScope.launch {
            val response = repository.getUser(createdBy)
            if(response.isSuccessful){
                val data = response.body()!!
                data.forEach { superUser ->
                    if(superUser.id == createdBy){
                        _createdByUser.postValue(superUser)
                        return@forEach
                    }
                }
            }
        }
    }
}