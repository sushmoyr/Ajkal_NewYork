package com.sushmoyr.ajkalnewyork.fragments.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(private val repository: Repository): ViewModel() {

    val allCategory = MutableLiveData<Response<List<Category>>>()

    fun getAllCats(){
        viewModelScope.launch(Dispatchers.IO) {
            allCategory.postValue(repository.getAllCategory())
        }
    }

}