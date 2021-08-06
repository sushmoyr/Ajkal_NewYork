package com.sushmoyr.ajkalnewyork.fragments.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.models.core.BreakingNews
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.repository.Repository
import com.sushmoyr.ajkalnewyork.utils.Constants.MINIMUM_GALLERY_HEIGHT
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response

class HomeViewModel(private val _repository: Repository) : ViewModel() {
    private val repository = _repository.remoteDataSource
    val allCategory = MutableLiveData<Response<List<Category>>>()

    fun getAllCats() {
        viewModelScope.launch(Dispatchers.IO) {
            allCategory.postValue(repository.getAllCategory())
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

    fun getHomeItems(categoryId: String? = null) {

        if (categoryId == null) {
            Log.d("home", "null cat id")
        } else {
            Log.d("home", "Cat id = $categoryId")
        }
        viewModelScope.launch {
            val adsDeferred = async { repository.getAllAds() }
            val newsDeferred = async {
                when (categoryId) {
                    null -> repository.getAllNews()
                    else -> repository.getAllNews(categoryId)
                }
            }
            val photosDeferred = async { repository.getPhotos() }

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
                val adCount = 2

                news.body()!!.forEach {
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

                val photos = photosDeferred.await()
                if (photos.isSuccessful) {
                    photos.body()!!.forEach {
                        Log.d("gallery", "====================")
                        Log.d("gallery", "id: ${it.id}")
                        Log.d("gallery", "caption: ${it.photoTitle}")
                        Log.d("gallery", "image: ${it.imagePath}")
                    }
                    val photoData = DataModel.GalleryItem(photos.body()!!)
                    val index = if (homeItemList.size < MINIMUM_GALLERY_HEIGHT) {
                        homeItemList.size
                    } else {
                        MINIMUM_GALLERY_HEIGHT
                    }
                    homeItemList.add(index, photoData)
                }


                homeItems.postValue(homeItemList)
            }
        }
    }

    //breaking news section

    val breakingNewsObserve = MutableLiveData<BreakingNews?>()
    var runFlow: Boolean = true
    fun getBreakingNews() {
        viewModelScope.launch {
            val response = repository.getBreakingNews()
            var newsFlow = flowOf<BreakingNews>()

            //val breakingNewsList = mutableListOf<News>()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val breakingNewsData = response.body()!!
                Log.d("breaking", response.body()!!.size.toString())
                newsFlow = flow {
                    while (runFlow) {
                        breakingNewsData.forEach {
                            emit(it)
                            Log.d("breaking", "Emit data of id: ${it.newsId}")
                            delay(20000)
                        }
                    }
                }

                newsFlow.collect {
                    if (breakingNewsObserve.value != it)
                        breakingNewsObserve.postValue(it)
                }

            } else {
                Log.d("breaking", "${response.code()} : ${response.message()}")
                breakingNewsObserve.postValue(null)
            }
        }
    }

    var onFinished: ((news: News) -> Unit)? = null

    fun getNewsById(newsId: String?) {
        viewModelScope.launch {
            if (newsId != null) {
                val data = repository.getNewsById(newsId)
                if (data.isSuccessful) {
                    data.body()!!.forEach {
                        if (it.id == newsId) {
                            onFinished?.invoke(it)
                            return@forEach
                        }
                    }
                }
            }
        }
    }


}