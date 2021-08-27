package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.NetworkResponse
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.launch

class ArchiveViewModel: ViewModel() {

    private val repository = Repository().remoteDataSource

    init {
        getArchivedNews()
    }

    private val _archiveNews = MutableLiveData<List<News>>().also {
        it.value = null
    }
    val archiveNews get() = _archiveNews

    private fun getArchivedNews(){
        viewModelScope.launch {
            val newsResponse: NetworkResponse<List<News>> = repository.getArchivedNews()
            if(newsResponse is NetworkResponse.Success){
                val newsData = newsResponse.response
                _archiveNews.postValue(newsData)
            }
            else{
                _archiveNews.postValue(null)
            }
        }
    }

}