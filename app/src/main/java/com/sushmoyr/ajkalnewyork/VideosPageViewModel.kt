package com.sushmoyr.ajkalnewyork

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sushmoyr.ajkalnewyork.models.Video
import kotlinx.coroutines.launch

class VideosPageViewModel(application: Application): AndroidViewModel(application) {

    val allVideos = MutableLiveData<List<Video>>()

    init {
        setVideoData()
    }

    private fun setVideoData() {
        viewModelScope.launch {
            allVideos.value = parseJson()
        }
    }

    private fun parseJson(): List<Video> {
        val bufferReader =
            getApplication<Application>().assets.open("sample_video_data.json").bufferedReader()
        val data = bufferReader.use {
            it.readText()
        }

        val gson = Gson()
        val videoType = object : TypeToken<List<Video>>() {}.type

        return gson.fromJson(data, videoType)
    }
}