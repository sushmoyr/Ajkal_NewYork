package com.sushmoyr.ajkalnewyork.activities.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.NetworkResponse
import com.sushmoyr.ajkalnewyork.models.DrawerItemModel
import com.sushmoyr.ajkalnewyork.models.UploadResponse
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.utility.NewsLetterResponse
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel(private val repository: RemoteDataSource) : ViewModel() {
    val allCategories = MutableLiveData<Response<List<Category>>>()
    val drawerItemList = MutableLiveData<List<DrawerItemModel>>()
    var categoryData :List<Category> = listOf()

    suspend fun subscribeNewsLetter(email: String): NetworkResponse<NewsLetterResponse>{
        return repository.subscribeNewsLetter(email)
    }

    var errorListener: ((e: Exception?) -> Unit)? = null

    fun getAllCats() {
        Log.d("debugNext", "called from Activity")
        viewModelScope.launch {
            when (val response = repository.getAllCategory()) {
                is NetworkResponse.Error -> errorListener?.invoke(response.exception)
                is NetworkResponse.Success -> {
                    allCategories.postValue(response.response!!)
                    if(response.response.isSuccessful && response.response.body() != null){
                        categoryData = response.response.body()!!
                    }
                }
            }
        }
    }

    fun prepareDrawerItems() {
        viewModelScope.launch {
            val catsDeferred = async { repository.getAllCategory() }
            val subDeferred = async { repository.getAllSubCategory() }

            val catsResponse = catsDeferred.await()
            val subResponse = subDeferred.await()

            if (catsResponse is NetworkResponse.Success && subResponse is NetworkResponse.Success) {

                if (catsResponse.response?.isSuccessful!! && subResponse.response?.isSuccessful!!) {
                    val categoryList = catsResponse.response.body()!!
                    val subCategoryList = subResponse.response.body()!!

                    val drawerItems: MutableList<DrawerItemModel> = mutableListOf()

                    categoryList.forEach { category ->
                        val subcategory = subCategoryList.filter { it.categoryId == category.id }
                        drawerItems.add(DrawerItemModel(category, subcategory))
                    }

                    drawerItemList.postValue(drawerItems)

                }

            }
            else{
                Log.d("exception", "Unknown exception at prepare drawer items")
                errorListener?.invoke(catsResponse.exception)
                errorListener?.invoke(subResponse.exception)
            }
        }


    }


}