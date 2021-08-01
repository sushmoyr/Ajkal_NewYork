package com.sushmoyr.ajkalnewyork.fragments.trending

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TrendingViewModel(private val repository: Repository): ViewModel() {

    val trendingItems = MutableLiveData<List<DataModel>>()

    fun getTrendingNews(categoryId: Int? = null) {
        viewModelScope.launch {
            val adsDeferred = async { repository.getAllAds() }
            val newsDeferred = async {
                when (categoryId) {
                    null -> repository.getTrendingNews()
                    else -> repository.getTrendingNews(categoryId)
                }
            }
            /*val photosDeferred = async { repository.getPhotos() }*/

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

                /*val photos = photosDeferred.await()
                if (photos.isSuccessful) {
                    photos.body()!!.forEach {
                        Log.d("gallery", "====================")
                        Log.d("gallery", "id: ${it.id}")
                        Log.d("gallery", "caption: ${it.caption}")
                        Log.d("gallery", "image: ${it.imagePath}")
                    }
                    val photoData = DataModel.GalleryItem(photos.body()!!)
                    photoData.images.forEach {

                    }
                    //homeItemList.add(17, photoData)
                }*/


                trendingItems.postValue(homeItemList)

            }
        }
    }
}