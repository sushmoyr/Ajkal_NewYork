package com.sushmoyr.ajkalnewyork.fragments.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.core.Photo
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class GalleryViewModel : ViewModel() {

    private val repository = Repository()
    private val fullGalleryImages = MutableLiveData<Response<List<Photo>>>()
    val galleryImages get() = fullGalleryImages

    fun getImages(){
        viewModelScope.launch {
            val deferred = async { repository.remoteDataSource.getFullGallery() }
            val data = deferred.await()
            fullGalleryImages.postValue(data)
        }
    }
}