package com.sushmoyr.ajkalnewyork.activities.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource

class MainActivityViewModelFactory(private val repository: RemoteDataSource):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}