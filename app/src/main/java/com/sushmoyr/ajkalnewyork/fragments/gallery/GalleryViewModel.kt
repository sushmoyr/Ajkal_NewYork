package com.sushmoyr.ajkalnewyork.fragments.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.Photo
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class GalleryViewModel(): ViewModel() {

    private val repository = Repository()
    private val fullGalleryImages = MutableLiveData<List<Photo>>()
    val galleryImages get() = fullGalleryImages

    fun getImages(){
        viewModelScope.launch {
            val deferred = async { repository.getFullGallery() }
            val data = deferred.await()
            if(data.isSuccessful){
                fullGalleryImages.postValue(data.body()!!)
            }
        }
    }
}