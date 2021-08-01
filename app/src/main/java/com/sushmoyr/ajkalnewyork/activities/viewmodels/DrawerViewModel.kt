package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sushmoyr.ajkalnewyork.models.Category

class DrawerViewModel: ViewModel() {
    var data: String = ""
    var selectedCategory = MutableLiveData<String>()
    var categoryListData = emptyList<Category>()
    fun setValue(msg: String){
        selectedCategory.postValue(msg)
    }

    fun setCategoryList(categoryList: List<Category>){
        categoryListData = categoryList
    }
}