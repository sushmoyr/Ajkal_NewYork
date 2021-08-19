package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sushmoyr.ajkalnewyork.models.core.Category

class DrawerViewModel: ViewModel() {
    var data: String = ""
    var selectedCategory = MutableLiveData<String>()
    var categoryListData = emptyList<Category>()
    fun selectedCategoryFilter(msg: String){
        selectedCategory.postValue(msg)
    }

    fun setCategoryList(categoryList: List<Category>){
        categoryListData = categoryList
    }

    fun getCategoryList() = MutableLiveData<List<Category>>(categoryListData)
}