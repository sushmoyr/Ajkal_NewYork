package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.DrawerItemModel
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.core.SubCategory
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel(private val repository: RemoteDataSource): ViewModel() {
    val allCategories = MutableLiveData<Response<List<Category>>>()
    val drawerItemList = MutableLiveData<List<DrawerItemModel>>()

    fun getAllCats(){
        viewModelScope.launch(Dispatchers.IO) {
            allCategories.postValue(repository.getAllCategory())
        }
    }

    fun prepareDrawerItems(){
        viewModelScope.launch{
            val catsDeferred = async { repository.getAllCategory() }
            val subDeferred = async { repository.getAllSubCategory() }

            val catsResponse = catsDeferred.await()
            val subResponse = subDeferred.await()

            if(catsResponse.isSuccessful && subResponse.isSuccessful){
                val categoryList = catsResponse.body()!!
                val subCategoryList = subResponse.body()!!

                val drawerItems : MutableList<DrawerItemModel> = mutableListOf()

                categoryList.forEach { category ->
                    val subcategory = subCategoryList.filter { it.categoryId == category.id }
                    drawerItems.add(DrawerItemModel(category, subcategory))
                }

                drawerItemList.postValue(drawerItems)
            }

        }
    }


}