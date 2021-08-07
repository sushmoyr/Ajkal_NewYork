package com.sushmoyr.ajkalnewyork.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.core.Video
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.launch

class VideosPageViewModel(application: Application): AndroidViewModel(application) {

    val allVideos = MutableLiveData<List<Video>>()
    private val repository = Repository()

    init {
        setVideoData()
    }

    private fun setVideoData() {
        viewModelScope.launch {
            val videoList = repository.remoteDataSource.getAllVideos()
            if(videoList.isSuccessful){
                allVideos.postValue(videoList.body()!!)
            }
        }
    }
}