package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.repository.Repository
import com.sushmoyr.ajkalnewyork.utils.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {

    private val _newsList = MutableLiveData<List<News>>().also {
        it.value = null
    }
    val newsList get() = _newsList
    private val repository = Repository().remoteDataSource

    var searchState = MutableLiveData<SearchState>().also {
        it.value = SearchState.Result(false)
    }

    fun searchNews(query: String){
        viewModelScope.launch(Dispatchers.IO) {
            searchState.postValue(SearchState.Loading(true))
            val response = repository.getAllNewsCore()
            searchState.postValue(SearchState.Loading(false))
            if(response.isSuccessful && response.body() != null){
                val data = response.body()!!
                val filteredData = data.filter {
                    it.newsTitle.contains(query, true)
                }
                _newsList.postValue(filteredData)
                searchState.postValue(SearchState.Result(!filteredData.isNullOrEmpty()))
            }
        }
    }
}