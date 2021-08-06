package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel(private val repository: RemoteDataSource): ViewModel() {
    val allCategories = MutableLiveData<Response<List<Category>>>()

    fun getAllCats(){
        viewModelScope.launch(Dispatchers.IO) {
            allCategories.postValue(repository.getAllCategory())
        }
    }
}