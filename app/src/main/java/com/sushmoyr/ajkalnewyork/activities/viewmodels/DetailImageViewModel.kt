package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.ViewModel
import com.sushmoyr.ajkalnewyork.models.core.Photo

class DetailImageViewModel: ViewModel() {
    private var _displayImage: Photo? = null
    val displayImage get() = _displayImage

    fun setDisplayImage(photo: Photo){
        _displayImage = photo
    }
}