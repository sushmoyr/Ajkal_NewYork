package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sushmoyr.ajkalnewyork.repository.Repository

class NewsDetailActivityViewModelFactory(private val repository: Repository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsDetailViewModel(repository) as T
    }

}